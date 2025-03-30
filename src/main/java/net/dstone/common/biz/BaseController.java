package net.dstone.common.biz;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import net.dstone.common.consts.ErrCd;
import net.dstone.common.utils.RequestUtil;
import net.dstone.common.utils.StringUtil;

@Controller
public class BaseController extends net.dstone.common.core.BaseObject {

	public static String RETURN_SUCCESS = "0";
	public static String RETURN_FAIL 	= "1"; 
	
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
						if( fieldValue != null ) {
							BeanUtils.setProperty(bean, fieldName, fieldValue);
						}
					}else{
						fieldValue = request.getParameter(fieldName);
						if(!StringUtil.isEmpty(fieldValue)) {
							BeanUtils.setProperty(bean, fieldName, fieldValue);
						}
					}
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
	 * 간단 암/복호화 Key. 16자리수.
	 */
	private static String SIMPLE_ENCRYPT_KEY = "db2admindb2admin";
	
	/**
     * 간단 암호화 메소드.
     * @return
     */
	protected String getSimpleEnc(String input) {
		String output = "";
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding");
			javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(SIMPLE_ENCRYPT_KEY.getBytes(), "AES");
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey);
			output = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	/**
     * 간단 복호화 메소드.
     * @return
     */
	protected String getSimpleDec(String input) {
		String output = "";
		try {
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding");
			javax.crypto.SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(SIMPLE_ENCRYPT_KEY.getBytes(), "AES");
			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);
			output = new String(cipher.doFinal(java.util.Base64.getDecoder().decode(input)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
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
    

	private static ArrayList<String> PROXY_ALLOWED_HEADER_KEY_LIST = new ArrayList<String>();
	static {
		PROXY_ALLOWED_HEADER_KEY_LIST.add("Accept-Encoding");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("eformsign_signature");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("Authorization");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("X-Naver-Client-Id");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("X-Naver-Client-Secret");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("x-ncp-apigw-api-key");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("x-ncp-apigw-api-key-id");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("client-id");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("client-secret");
		PROXY_ALLOWED_HEADER_KEY_LIST.add("productID");
	}

	/**
	 * RestTemplate 을 이용한 Proxy 기능 메소드
	 * @param request
	 * @param response
	 * @param exception
	 */
    @RequestMapping(value = "/proxy.do") 
	protected ResponseEntity proxy(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception{

    	info( this.getClass().getName() + ".proxy has been called !!!" );

    	RestTemplate restTemplate = null;
    	HttpEntity<?> requestEntity = null;
    	ResponseEntity<?> responseEntity = null;
       
        try {
        	String realUrl = getSimpleDec( request.getParameter("realUrl") );
        	info( this.getClass().getName() + ".proxy realUrl["+realUrl+"]" );

			/* 1. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				if( PROXY_ALLOWED_HEADER_KEY_LIST.size() == 0 ) {
					httpHeaders.add(hKey, request.getHeader(hKey));
				}else if( PROXY_ALLOWED_HEADER_KEY_LIST.contains(hKey) ) {
					httpHeaders.add(hKey, request.getHeader(hKey));
				}
			}
			httpHeaders.setContentType(MediaType.valueOf(request.getContentType()));

			/* 2. Body 및 HttpEntity 생성 */
			// METHOD - POST, PUT 방식
			if( request.getMethod().toUpperCase().equals("POST") || request.getMethod().toUpperCase().equals("PUT") ) {
				// Json 방식
				if( httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON) ||  httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON_UTF8) ) {
					String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
					requestEntity = new HttpEntity<String>(body, httpHeaders);					
				// Form	방식
				}else {
					MultiValueMap<String, Object> reqParam = new LinkedMultiValueMap<>();
					java.util.Enumeration<String> keys = request.getParameterNames();
					while( keys.hasMoreElements() ) {
						String key = keys.nextElement();
						if( request.getParameterValues(key).length > 1) {
							String[] valArr = request.getParameterValues(key);
							for( String val : valArr ) {
								reqParam.add(key, val);
							}
						}else {
							reqParam.add(key, request.getParameter(key));
						}
					}
					requestEntity = new HttpEntity<MultiValueMap>(reqParam, httpHeaders);		
				}
			// METHOD - 나머지 방식
			}else{
				requestEntity = new HttpEntity<String>("", httpHeaders);
			}
			
			/* 3. RestTemplate 호출 */
	    	restTemplate = net.dstone.common.utils.RestFulUtil.getInstance().getRestTemplate();
			responseEntity = restTemplate.exchange(
				realUrl
				, HttpMethod.valueOf(request.getMethod())
				, requestEntity
				, String.class
            );
			
        } catch (Exception e) {
            e.printStackTrace();
        }
 
		return responseEntity;
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
