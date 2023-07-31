package net.dstone.common.exception.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import net.dstone.common.biz.BaseController;
import net.dstone.common.conts.ErrCd;
import net.dstone.common.exception.BizException;
import net.dstone.common.exception.SecException;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.RequestUtil;

public class DsExceptionResolver extends SimpleMappingExceptionResolver {

	private static final LogUtil logger = new LogUtil(DsExceptionResolver.class);

	private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.info(this.getClass().getName() + ".doResolveException() has been called !!!");
		ModelAndView mav = null;
		this.applyErrCdToResponse(response, ex);
		String viewName = this.determineViewName(ex, request);
		if( viewName != null ) {
			mav = this.getModelAndView(viewName, ex, request);
			mav.addObject("ERR_CD", response.getHeader("ERR_CD"));
			mav.addObject("ERR_MSG", response.getHeader("ERR_MSG"));
			StringBuffer errMsgDetail = new StringBuffer();
			StackTraceElement[] stackTraceElementList = ex.getStackTrace();
			if(stackTraceElementList != null) {
				for( StackTraceElement item : stackTraceElementList) {
					errMsgDetail.append(item.toString()).append("\n");
				}
			}
			mav.addObject("ERR_MSG_DETAIL", errMsgDetail.toString());
			if(RequestUtil.isAjax(request)) {
				Integer statusCode = this.determineStatusCode(request, viewName);
				if( statusCode != null ) {
					this.addStatusCode(viewName, statusCode);
				}
			}
		}
		this.dumpExceptionLog(ex);
		return mav;
	}
	
	private void dumpExceptionLog(Exception ex) {
		ex.printStackTrace();
	}
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(viewName);
		if(RequestUtil.isAjax(request)) { 
			mav = new ModelAndView("jsonView"); 
		}
		return mav;
	}

	private void applyErrCdToResponse(HttpServletResponse response, Exception ex) {
		try {
			if(ex instanceof SecException) {
				BaseController.setErrCd(response, ((SecException)ex).getErrCd());
			}else if(ex instanceof BizException) {
				BaseController.setErrCd(response, ((BizException)ex).getErrCd());
			}else {
				BaseController.setErrCd(response, ErrCd.SYS_ERR);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
