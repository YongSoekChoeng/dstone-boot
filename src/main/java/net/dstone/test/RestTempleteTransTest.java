package net.dstone.test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import net.dstone.common.utils.DataSet;

@Controller
@RequestMapping(value = "/test/rest/*")
public class RestTempleteTransTest extends net.dstone.common.core.BaseObject {
	
	@RequestMapping(value = "/callTest.do") 
    public ResponseEntity callTest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {

    	info( this.getClass().getName() + ".callTest has been called !!!" );
    	
    	RestTemplate restTemplate = null;
    	HttpEntity<String> requestEntity = null;
    	ResponseEntity<String> responseEntity = null;
    	
		try {
			/* 1. Header 생성 */
			HttpHeaders httpHeaders = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				httpHeaders.add(hKey, request.getHeader(hKey));
			}

	    	//info( this.getClass().getName() + ".callTest =================================>>> line 53 request.getParameterMap():" + request.getParameterMap());
	    	
			/* 2. Body 생성 */
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
			rowDs.checkData();
	    	
			/* 3. HttpEntity 생성 */
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			requestEntity = new HttpEntity<String>(resDs.toJson(), httpHeaders);
	    	info( this.getClass().getName() + ".callTest =================================>>> line 62 requestEntity.getBody():" + requestEntity.getBody() );

			/* 4. RestTemplate 생성 */
			restTemplate = net.dstone.common.utils.RestFulUtil.getInstance().getRestTemplate();

			/* 5. RestTemplate 호출 */
	    	info( this.getClass().getName() + ".callTest =================================>>> line 69 request.getMethod():" + request.getMethod() );
	    	if( "POST".equals(request.getMethod().toUpperCase()) || "PUT".equals(request.getMethod().toUpperCase()) ) {
	    		responseEntity = restTemplate.postForEntity("http://localhost:7081/test/rest/callProxy.do", requestEntity, String.class);
	    	}else {
	    		responseEntity = restTemplate.getForEntity("http://localhost:7081/test/rest/callProxy.do", String.class);
	    	}

			System.out.println( "callTest ::: responseEntity===>>>" + responseEntity );
			System.out.println( "callTest ::: responseEntity.getBody()===>>>" + responseEntity.getBody() );
			
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
			HttpHeaders headers = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				headers.add(hKey, request.getHeader(hKey));
			}
			
        	String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
			
			responseEntity = restTemplate.exchange(
				"http://localhost:7081/test/rest/callMain.do",
                HttpMethod.valueOf(request.getMethod()),
                new HttpEntity<>(body, headers),
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

			/* 1. Header 생성 */
			HttpHeaders headers = new HttpHeaders();
			java.util.Enumeration<String> reqHeaderKeys = request.getHeaderNames();
			while( reqHeaderKeys.hasMoreElements() ) {
				String hKey = reqHeaderKeys.nextElement();
				headers.add(hKey, request.getHeader(hKey));
			}
			
			/* 2. Body 생성 */
        	String body = IOUtils.toString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
        	DataSet resDs = new DataSet();
        	resDs.buildFromJson(body, "");
        	for(int i=0; i<resDs.getDataSetRowCount("FRUIT_LIST"); i++) {
        		DataSet reqRowDs = resDs.getDataSet("FRUIT_LIST", i);
        		reqRowDs.setDatum("SUM", String.valueOf( (Integer.parseInt(reqRowDs.getDatum("PRICE")) * Integer.parseInt(reqRowDs.getDatum("PCS")) ) ));
        	}
        	
			/* 3. ResponseEntity 생성 */
			responseEntity = new ResponseEntity<String>(resDs.toJson(), headers, HttpStatus.valueOf(200));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseEntity;
	}
	
}
