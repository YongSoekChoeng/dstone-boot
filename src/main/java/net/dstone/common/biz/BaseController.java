package net.dstone.common.biz;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import net.dstone.common.conts.ErrCd;
import net.dstone.common.utils.RequestUtil;

@Controller
public class BaseController extends net.dstone.common.core.BaseObject {

	protected String nullCheck(Object o) {
		return net.dstone.common.utils.StringUtil.nullCheck(o, "");
	}

	/**
	 * 리퀘스트의 값을 빈객체에 세팅하여 반환 .(단건용)
	 * @param request
	 * @param bean
	 * @return
	 */
	protected Object bindSingleValue(RequestUtil request, Object bean) {
		Class<?> clz = null;
		java.lang.reflect.Field[] fields = null;
		java.lang.reflect.Field field = null;
		String fieldName = "";
		Object fieldValue = null;
		boolean isArray = false;
		try {
			clz = bean.getClass(); 
			fields = clz.getDeclaredFields();
			if(fields != null){
				for(int i=0; i<fields.length; i++ ){
					field = fields[i];
					fieldName = field.getName();					
					isArray = bean.getClass().getDeclaredField(fieldName).getType().isArray();
					if(isArray){
						fieldValue = request.getParameterValues(fieldName);
					}else{
						fieldValue = request.getParameter(fieldName);
					}
					BeanUtils.setProperty(bean, fieldName, fieldValue);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 리퀘스트의 값을 빈객체에 세팅하여 반환 .(다건용)
	 * @param request
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object bindMultiValues(net.dstone.common.utils.RequestUtil request, String beanName) {
		Object[] beanArray = null;
		Class<?> clz = null;
		Object bean = null;
		java.lang.reflect.Field[] fields = null;
		java.lang.reflect.Field field = null;
		java.util.Properties fieldProp = new java.util.Properties();
		int maxArrayNum = 0;
		String paramName = "";
		String paramValue = "";
		String[] paramValues = null;
		java.util.Vector vec = new java.util.Vector(); 

		try {
			clz = Class.forName(beanName); 
			fields = clz.getDeclaredFields();

			if(fields != null){
				for(int i=0; i<fields.length; i++ ){
					field = fields[i];
					paramName = field.getName();
					paramValues = request.getParameterValues(paramName);
					if(paramValues != null){
						fieldProp.put(paramName, paramValues);
						if(paramValues.length>maxArrayNum){
							maxArrayNum = paramValues.length;
						}
					}
				}
				for(int i=0; i<maxArrayNum; i++ ){
					bean = clz.newInstance();
					for(int k=0; k<fields.length; k++ ){
						field = fields[k];
						paramName = field.getName();
						paramValues = (String[])fieldProp.get(paramName);
						if(paramValues != null && paramValues.length > i ){
							paramValue = paramValues[i];
							try {            
								clz.getMethod("set" + paramName, new Class[]{ paramValue.getClass()}).invoke(bean, paramValue);               
							} catch (Exception ex) {
								//ex.printStackTrace();
							}
						}else{
							paramValue = null;
						}
					}
					vec.add(bean);
				}	
				beanArray = (Object[])java.lang.reflect.Array.newInstance(clz, vec.size());
				vec.copyInto(beanArray);
				vec.clear(); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return beanArray;
	}
	
	/**
     * Ajax 방식일 때 사용 할 View를 생성.
     * @return
     */
    @Bean
    protected MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }


	/**
	 * 기본링크 메소드
	 * @param request
	 * @param response
	 * @param exception
	 */
    @RequestMapping(value = "/defaultLink.do") 
	protected ModelAndView defaultLink(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ModelAndView mav) throws Exception{

    	/************************ 변수 선언 시작 ************************/
   		net.dstone.common.utils.RequestUtil 					requestUtil;
   		/************************ 변수 선언 끝 **************************/

		/************************ 변수 정의 시작 ************************/
		requestUtil 			= new net.dstone.common.utils.RequestUtil(request, response);
		/************************ 변수 정의 끝 ************************/
		
		/************************ 컨트롤러 로직 시작 ************************/
		mav.setViewName(requestUtil.getParameter("defaultLink", ""));
		/************************ 컨트롤러 로직 끝 ************************/
			
   		return mav;
	}

	protected boolean isAjax(HttpServletRequest request) {
		boolean isAjax = false;
		if(RequestUtil.isAjax(request)) {
			isAjax = true;
		}
		return isAjax;
	}
	protected boolean isJson(HttpServletRequest request) {
		boolean isAjax = false;
		if(RequestUtil.isAjax(request)) {
			isAjax = true;
		}
		return isAjax;
	}

	protected void setForcedToUrl(HttpServletResponse response, String forcedToUrl) {
		response.setHeader("FORCED_TO_URL", forcedToUrl);
	}
	
	@SuppressWarnings("deprecation")
	public static void setErrCd(HttpServletRequest request, HttpServletResponse response, ErrCd errCd) {
		response.setHeader("SUCCESS_YN", "N");
		response.setHeader("ERR_CD", errCd.getErrCd());
		if( RequestUtil.isAjax(request) ) {
			response.setHeader("ERR_MSG", URLEncoder.encode(errCd.getErrMsg()).replaceAll("\\+", "%20"));
		}else {
			response.setHeader("ERR_MSG", errCd.getErrMsg());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void setErrCd(HttpServletRequest request, HttpServletResponse response, String strErrCd) {
		response.setHeader("SUCCESS_YN", "N");
		ErrCd errCd = ErrCd.getErrCdByCd(strErrCd);
		if( errCd != null ) {
			response.setHeader("ERR_CD", errCd.getErrCd());
			if( RequestUtil.isAjax(request) ) {
				response.setHeader("ERR_MSG", URLEncoder.encode(errCd.getErrMsg()).replaceAll("\\+", "%20"));
			}else {
				response.setHeader("ERR_MSG", errCd.getErrMsg());
			}
		}
	}
	
}
