package net.dstone.test;

import java.nio.charset.Charset;
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
			httpHeaders.setContentType(MediaType.valueOf(request.getContentType()));

			/* 2. Body 및 HttpEntity 생성 */
			// Json 방식
			if( httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON) ||  httpHeaders.getContentType().equals(MediaType.APPLICATION_JSON_UTF8) ) {
				DataSet reqDs = new DataSet();
				reqDs.setDatum("TEST", "과일목록");
				requestEntity = new HttpEntity<String>(reqDs.toJson(), httpHeaders);
			// Form	방식
			}else {
				MultiValueMap<String, String> reqParam = new LinkedMultiValueMap<>();
				reqParam.add("TEST", "과일목록");
				requestEntity = new HttpEntity<Map>(reqParam, httpHeaders);
			}
			
	    	info( this.getClass().getName() + ".callTest =================================>>> line 62 requestEntity.getBody():" + requestEntity.getBody() );

			/* 3. RestTemplate 생성 */
			restTemplate = net.dstone.common.utils.RestFulUtil.getInstance().getRestTemplate();

			/* 4. RestTemplate 호출 */
			responseEntity = restTemplate.exchange(
				"http://localhost:7081/test/rest/callProxy.do",
	             HttpMethod.valueOf(request.getMethod()),
	             requestEntity,
	             String.class
	        );

			System.out.println( "callTest ::: responseEntity===>>>" + responseEntity );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
	}
	
	
    @RequestMapping(value = "/callProxy.do") 
    public ResponseEntity callProxy(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

    	info( this.getClass().getName() + ".callProxyPost has been called !!!" );

    	RestTemplate restTemplate = net.dstone.common.utils.RestFulUtil.getInstance().getRestTemplate();
    	ResponseEntity<String> responseEntity = null;
       
        try {
        	
			/* 1. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				httpHeaders.add(hKey, request.getHeader(hKey));
			}
			
        	String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
			
			responseEntity = restTemplate.exchange(
				"http://localhost:7081/test/rest/callMain.do",
                HttpMethod.valueOf(request.getMethod()),
                new HttpEntity<>(body, httpHeaders),
                String.class
            );

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
        	String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
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
