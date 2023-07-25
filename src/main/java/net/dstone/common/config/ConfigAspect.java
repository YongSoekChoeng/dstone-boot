package net.dstone.common.config;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.StringUtil;

@Aspect
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableEncryptableProperties
public class ConfigAspect {

	private static final LogUtil logger = new LogUtil(ConfigAspect.class);

	/**************************************** 1. Logging 관련 AOP ****************************************/
	@SuppressWarnings("static-access")
	@Around("execution(* net.dstone.*..*Controller.*(..))")
	public Object doControllerProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.sysout("\n\n||===================================== [" + joinPoint.getTarget().getClass().getName() + "] START ======================================||");
		logger.info("+->[CONTROLLER] {"+buildSimpleExecutionInfo(joinPoint, "")+"}");
		
		/*****************************************************************************************************
		컨트롤러 호출 시 응답헤더에 기본값 세팅
		  - Response 헤더[SUCCESS_YN]에 "Y"를 자동세팅한다. 컨크롤러 로직 수행중 오류 발생 시(setErrCd 호출 시) 자동으로 "N"으로 세팅된다.
		  - Exception 발생 시 DsExceptionResolver에 의해 Response 헤더[SUCCESS_YN]는 N"으로 자동세팅된다.
		*****************************************************************************************************/
		Object[] args = joinPoint.getArgs();
		if(args != null) {
			for(Object arg : args) {
				if(arg instanceof HttpServletResponse) {
					((HttpServletResponse)arg).setHeader("SUCCESS_YN", "Y");
					break;
				}
			}
		}
		/*****************************************************************************************************
		객체 실행
		*****************************************************************************************************/
		Object retObj = joinPoint.proceed();
		logger.sysout("||===================================== [" + joinPoint.getTarget().getClass().getName() + "] END ======================================||\n");
		return retObj;
	}

	@Around("execution(* net.dstone.*..*Service*.*(..))")
	public Object doServiceProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("+--->[SERVICE ] {"+buildSimpleExecutionInfo(joinPoint, "")+"}");
		return joinPoint.proceed();
	}

	@Around("execution(* net.dstone.*..*Dao.*(..))")
	public Object doDaoProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("+----->[DAO   ] {"+buildSimpleExecutionInfo(joinPoint, "")+"}");
		return joinPoint.proceed();
	}

	private String buildSimpleExecutionInfo(ProceedingJoinPoint joinPoint, String tabSpace) {
		StringBuffer buffer = new StringBuffer();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		StringBuffer paramListInfo = new StringBuffer();
		int args = joinPoint.getArgs().length;
		for (int i = 0; i < args; i++) {
			Object param = joinPoint.getArgs()[i];

			if (param instanceof String) {
				paramListInfo.append("String" + "[" + param + "]");
			} else {
				String result = ToStringBuilder.reflectionToString(param, ToStringStyle.SHORT_PREFIX_STYLE);
				paramListInfo.append(result);
			}

			if (i < joinPoint.getArgs().length - 1) {
				paramListInfo.append(", ");
			}
		}
		buffer.append(className + "." + methodName + "(" + paramListInfo + ")");
		return splitToLines(buffer.toString(),  tabSpace);
	}
	
	private String splitToLines(String msg, String tabSpace) {
		StringBuffer buffer = new StringBuffer();
		String[] lines = StringUtil.toStrArray(msg, "\n");
		for(int i=0; i < lines.length; i++) {
			String line = lines[i];
			buffer.append(tabSpace).append(line);
			if(i < lines.length-1) {
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

}
