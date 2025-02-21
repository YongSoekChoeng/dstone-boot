package net.dstone.common.utils;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
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
			HTTP_CLIENT_FACTORY.setConnectTimeout(5000); // 커넥션 최대 시간
			HTTP_CLIENT_FACTORY.setReadTimeout(5000); // 읽기 최대 시간
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
		return this.getRestTemplate(DEFAULT_CHARSET);
	}
	
	public RestTemplate getRestTemplate(String charSet) {
		RestTemplate restTemplate = new RestTemplate(HTTP_CLIENT_FACTORY);
		for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
			if (messageConverter instanceof AllEncompassingFormHttpMessageConverter) {
				((AllEncompassingFormHttpMessageConverter) messageConverter).setCharset(Charset.forName(charSet));
				((AllEncompassingFormHttpMessageConverter) messageConverter)
						.setMultipartCharset(Charset.forName(charSet));
			}
		}
		return restTemplate;
	}
}
