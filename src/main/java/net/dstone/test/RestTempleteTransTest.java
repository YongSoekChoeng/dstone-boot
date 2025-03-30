package net.dstone.test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dstone.common.utils.DataSet;

@Controller
@RequestMapping(value = "/test/rest/*")
public class RestTempleteTransTest extends net.dstone.common.core.BaseObject {
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/callTest.do") 
    public ResponseEntity callTest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

    	info( this.getClass().getName() + ".callTest has been called !!!" );
    	
    	RestTemplate restTemplate = null;
    	HttpEntity<?> requestEntity = null;
    	ResponseEntity<?> responseEntity = null;
    	
		try {
			/* 1. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				httpHeaders.add(hKey, request.getHeader(hKey));
			}
			info("request.getContentType()====>>>["+request.getContentType()+"]");
			httpHeaders.setContentType(MediaType.valueOf(request.getContentType()));

			/* 2. Body 및 HttpEntity 생성 */
			// METHOD - POST, PUT 방식
			if( request.getMethod().toUpperCase().equals("POST") || request.getMethod().toUpperCase().equals("PUT") ) {
				// Json 방식
				if( httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON) ||  httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON_UTF8) ) {
					String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
info( this.getClass().getName() + ".callTest ::: body 라인 53===>>>" + body );
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
info( this.getClass().getName() + ".callTest ::: body 라인 68===>>>["+key+"]" + val );	
							}
						}else {
							reqParam.add(key, request.getParameter(key));
info( this.getClass().getName() + ".callTest ::: body 라인 72===>>>["+key+"]" + request.getParameter(key) );	
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
				"http://localhost:7081/test/rest/callProxy.do",
	             HttpMethod.valueOf(request.getMethod()),
	             requestEntity,
	             String.class
	        );

	    	info( this.getClass().getName() + ".callTest ::: responseEntity===>>>" + responseEntity );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
	}
	
	
    @RequestMapping(value = "/callProxy.do") 
    public ResponseEntity callProxy(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

    	info( this.getClass().getName() + ".callProxy has been called !!!" );

    	RestTemplate restTemplate = null;
    	HttpEntity<?> requestEntity = null;
    	ResponseEntity<?> responseEntity = null;
       
        try {
        	
			/* 1. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				httpHeaders.add(hKey, request.getHeader(hKey));
			}

			/* 2. Body 및 HttpEntity 생성 */
			// METHOD - POST, PUT 방식
			if( request.getMethod().toUpperCase().equals("POST") || request.getMethod().toUpperCase().equals("PUT") ) {
				// Json 방식
				if( httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON) ||  httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON_UTF8) ) {
					String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
info( this.getClass().getName() + ".callProxy ::: body 라인 126===>>>" + body );
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
info( this.getClass().getName() + ".callProxy ::: body 라인 138===>>>["+key+"]" + val );	
							}
						}else {
							reqParam.add(key, request.getParameter(key));
info( this.getClass().getName() + ".callProxy ::: body 라인 142===>>>["+key+"]" + request.getParameter(key) );	
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
				"http://localhost:7081/test/rest/callMain.do"
				, HttpMethod.valueOf(request.getMethod())
				, requestEntity
				, String.class
            );

	    	info( this.getClass().getName() + ".callProxy ::: responseEntity===>>>" + responseEntity );
			
        } catch (Exception e) {
            e.printStackTrace();
        }
 
		return responseEntity;
	}
    
    @RequestMapping(value = "/callMain.do") 
    public ResponseEntity callMain(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

    	info( this.getClass().getName() + ".callMain has been called !!!" );
    	
    	ResponseEntity responseEntity = null;
    	
		try {

			/* 1. Body 생성 */
			if( request.getMethod().toUpperCase().equals("POST") || request.getMethod().toUpperCase().equals("PUT") ) {
				// Json 방식
				if( request.getContentType().equals(MediaType.APPLICATION_JSON.getType()) ||  request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8.getType()) ) {
					String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
info( this.getClass().getName() + ".callMain ::: body 라인 154===>>>" + body.toString() );					
				// Form	방식
				}else {
					java.util.Enumeration<String> keys = request.getParameterNames();
					while( keys.hasMoreElements() ) {
						String key = keys.nextElement();
						String val = request.getParameter(key);
						if( request.getParameterValues(key)  != null && request.getParameterValues(key).length > 1) {
info( this.getClass().getName() + ".callMain ::: body 라인 163===>>>["+key+"]" + java.util.Arrays.toString(request.getParameterValues(key)) );	
						}else{
info( this.getClass().getName() + ".callMain ::: body 라인 165===>>>["+key+"]" + request.getParameter(key) );	
						}
					}
				}
			// METHOD - 나머지 방식
			}else{
				Map<String, String[]> reqMap = request.getParameterMap();
				java.util.Iterator<String> keys = reqMap.keySet().iterator();
				while( keys.hasNext() ) {
					String key = keys.next();
info( this.getClass().getName() + ".callMain ::: body 라인 170===>>>" + request.getParameter(key) );		
				}
			}
			
	    	info( this.getClass().getName() + ".callMain ::: request===>>>" + request );

			/* 1. Body 생성 */
        	DataSet resDs = new DataSet();
        	resDs.setDatum("NAME", "과일목록");
			DataSet rowDs = resDs.addDataSet("FRUIT_LIST");
			rowDs.setDatum("NAME", "사과");
			rowDs.setDatum("PRICE", "1000");
			rowDs.setDatum("PCS", "10");
			rowDs = resDs.addDataSet("FRUIT_LIST");
			rowDs.setDatum("NAME", "배");
			rowDs.setDatum("PRICE", "2000");
			rowDs.setDatum("PCS", "15");
			/* 2. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			httpHeaders.setContentLength(resDs.toJson().getBytes().length);
			
			/* 3. ResponseEntity 생성 */
			responseEntity = new ResponseEntity<String>(resDs.toJson(), httpHeaders, HttpStatus.valueOf(200));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
	}
	
}
