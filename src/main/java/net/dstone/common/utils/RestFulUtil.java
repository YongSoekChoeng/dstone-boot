package net.dstone.common.utils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestFulUtil {
	
	private static RestFulUtil restFulUtil = null;
	private HttpComponentsClientHttpRequestFactory HTTP_CLIENT_FACTORY;
	private static String DEFAULT_CHARSET = "UTF-8";
	
	private RestFulUtil() {
		init();
	}
	
	private void init() {
		try {
	        /******************************************
	        SSL 검증 비활성화 (로컬 환경에서만 사용)
	        // 모든 인증서 신뢰
	        SSLContext sslContext = SSLContextBuilder.create()
	                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
	                .build();
			******************************************/
	
			/***************** 설정 시작 *****************/
			HttpClient httpClient = HttpClientBuilder.create()
					//.setSSLContext(sslContext)
					.setMaxConnTotal(100) // 최대로 연결할 수 있는 커넥션 쓰레드 수
					.setMaxConnPerRoute(60) // (IP + PORT) 당 커넥션 쓰레드 수
					.evictIdleConnections(60L, TimeUnit.SECONDS) // 최대 연결 유효시간을 지정한다
					.evictExpiredConnections() // 설정한 최대 연결 유효시간이 만료되면 커넥션을 해제한다
					.setConnectionTimeToLive(5, TimeUnit.SECONDS) // 커넥션 만료시간을 설정한다
					.build();
			
			HTTP_CLIENT_FACTORY = new HttpComponentsClientHttpRequestFactory();
			HTTP_CLIENT_FACTORY.setConnectionRequestTimeout(5000 * 1000); //  HttpClient가 커넥션 풀에서 사용 가능한 연결을 가져오기 위해 대기하는 최대 시간을 지정하는 옵션. 커넥션 풀에서 사용 가능한 연결이 없을 때, 요청은 커넥션 풀에 새로운 연결이 생성될 때까지 해당 시간만큼 대기.
			HTTP_CLIENT_FACTORY.setConnectTimeout(30*1000); // 커넥션 최대 시간
			HTTP_CLIENT_FACTORY.setReadTimeout(30*1000); // 읽기 최대 시간
			HTTP_CLIENT_FACTORY.setHttpClient(httpClient);
			/***************** 설정 종료 *****************/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static RestFulUtil getInstance() {
		if( restFulUtil == null ) {
			restFulUtil = new RestFulUtil();
		}
		return restFulUtil;
	}
	
	public RestTemplate getRestTemplate() {
		int connectTimeout = 20; 			// 연결시도제한시간. 클라이언트가 서버에 TCP 연결을 시도할 때 걸리는 최대 시간을 의미. 서버가 아예 응답을 하지 않거나, DNS 해석이나 네트워크 문제가 있을 때 이 시간이 초과되면 예외가 발생.
		int readTimeout = 30;				// 응답대기시간. 서버와 연결이 된 이후, 응답 바디를 읽는 데 걸리는 최대 시간. 즉, 서버가 요청을 받았지만 응답을 늦게 보내거나 멈춘 경우 이 시간 안에 응답이 도착하지 않으면 예외가 발생.
		int connectionRequestTimeout = 20;	// 커넥션풀에서연결얻는시간. 커넥션 풀을 사용하는 경우, 풀에서 커넥션을 얻기 위해 기다리는 최대 시간.
		return this.getRestTemplate(DEFAULT_CHARSET, connectTimeout, readTimeout, connectionRequestTimeout);
	}
	
	/**
	 * RestTemplate 을 얻기위한 메소드.
	 * @param charSet 캐릭터셋.<br>
	 * @param connectTimeout 연결시도제한시간. 클라이언트가 서버에 TCP 연결을 시도할 때 걸리는 최대 시간을 의미. 서버가 아예 응답을 하지 않거나, DNS 해석이나 네트워크 문제가 있을 때 이 시간이 초과되면 예외가 발생.<br>
	 * @param readTimeout 응답대기시간. 서버와 연결이 된 이후, 응답 바디를 읽는 데 걸리는 최대 시간. 즉, 서버가 요청을 받았지만 응답을 늦게 보내거나 멈춘 경우 이 시간 안에 응답이 도착하지 않으면 예외가 발생.<br>
	 * @param connectionRequestTimeout 커넥션풀에서연결얻는시간. 커넥션 풀을 사용하는 경우, 풀에서 커넥션을 얻기 위해 기다리는 최대 시간.<br>
	 * @return
	 */
	public RestTemplate getRestTemplate(String charSet, int connectTimeout, int readTimeout, int connectionRequestTimeout) {
		
		RestTemplate restTemplate = null;
		
		/***************** 설정 시작 *****************/
        // 커넥션 풀 설정
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);            // 전체 최대 커넥션 수
        connectionManager.setDefaultMaxPerRoute(20);   // 라우트당 최대 커넥션 수

        // HttpClient 설정
        CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .evictIdleConnections(120, TimeUnit.SECONDS)  // 120초 이상 idle 연결 제거
            .build();

        // Factory에 HttpClient 적용 및 타임아웃 설정
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(connectTimeout * 1000);     					// 서버 연결까지 최대 timeout 초 대기
        factory.setReadTimeout(readTimeout * 1000);        						// 응답까지 최대 timeout 초 대기
        factory.setConnectionRequestTimeout(connectionRequestTimeout * 1000);  	// 커넥션 풀에서 커넥션을 얻기까지 최대 timeout 초 대기

        restTemplate = new RestTemplate(factory);
		/***************** 설정 종료 *****************/
		
		for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
			if (messageConverter instanceof AllEncompassingFormHttpMessageConverter) {
				((AllEncompassingFormHttpMessageConverter) messageConverter).setCharset(Charset.forName("UTF-8"));
				((AllEncompassingFormHttpMessageConverter) messageConverter).setMultipartCharset(Charset.forName("UTF-8"));
			}
		}
		RestResponseErrorHandler errorHandler = new RestResponseErrorHandler();
		errorHandler.setThrowExceptionOnError(false);
		restTemplate.setErrorHandler(errorHandler);
		
		return restTemplate;
	}
	
	public HttpHeaders getHeader(MediaType mediaType, Map<String, String> header) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { mediaType }));
		headers.setContentType(mediaType);
		if( header != null && header.size() >0 ) {
			Iterator<String> keys = header.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				headers.add(key, header.get(key));
			}
		}
		return headers;
	}
	
	public HttpEntity<String> getEntity(HttpHeaders headers, String input) {
		HttpEntity<String> entity = new HttpEntity<String>(input, headers);
		return entity;
	}

	public HttpEntity<MultiValueMap<String, Object>> getEntity(HttpHeaders headers, LinkedMultiValueMap input) {
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(input, headers);
		return entity;
	}
	
}
