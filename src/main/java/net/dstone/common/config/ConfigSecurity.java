package net.dstone.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.dstone.common.security.CustomAccessDeniedHandler;
import net.dstone.common.security.CustomAuthFailureHandler;
import net.dstone.common.security.CustomAuthSucessHandler;
import net.dstone.common.security.CustomAuthenticationProvider;
import net.dstone.common.security.CustomUsernamePasswordAuthenticationFilter;
import net.dstone.common.security.svc.CustomUserService;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.web.SessionListener;

@Configuration
@EnableWebSecurity
public class ConfigSecurity {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigSecurity.class);

	/* 화면으로 연결되는 경우 _PAGE로 끝나고 서버통신으로 연결되는 경우 _ACTION으로 끝난다. 화면은 확장자를 생략한다. */
	public static String MAIN_PAGE 						= "main";									// 메인 페이지
	public static String LOGIN_PAGE 					= "login";									// 로그인 페이지
	
	public static String LOGIN_CHECK_ACTION 			= "/com/login/loginCheck.do";				// 사용자가 로그인된 상태인지 체크하는 액션
	public static String LOGIN_GO_ACTION				= "/com/login/loginGo.do"; 					// 로그인 가기 액션(통과 후 로그인 페이지에 도달)
	public static String LOGIN_PROCESS_ACTION 			= "/com/login/loginProcess.do";				// 로그인 처리 액션
	public static String LOGIN_PROCESS_SUCCESS_ACTION 	= "/com/login/loginProcessSuccess.do";		// 로그인 처리 성공시 진행될 액션
	public static String LOGIN_PROCESS_FAILURE_ACTION 	= "/com/login/loginProcessFailure.do";		// 로그인 처리 실패시 진행될 액션
	public static String LOGOUT_ACTION 					= "/com/login/logout.do";					// 로그아웃 처리 액션
	public static String LOGOUT_SUCCS_ACTION 			= "/com/login/logoutSuccess.do";			// 로그아웃 처리 성공시 진행될 액션
	public static String ACCESS_DENIED_ACTION 			= "/com/login/accessDenied.do"; 			// 접근권한이 없을 시 진행될 액션
	public static String KAKAO_LOGIN_PAGE 				= "/kakao/*.do"; 							// 카카오로그인 액션
	public static String PROXY_ACTION 					= "/proxy.do"; 								// 프락시 액션

	public static String ERROR_URL_PATTERN				= "/error/**"; 								// 에러 URL패턴.(스프링 내부적으로 호출되는 에러 URL패턴 존재. Permit All로 설정)
	
	public static String USERNAME_PARAMETER 			= "USER_ID";
	public static String PASSWORD_PARAMETER 			= "USER_PW";
	public static String ROLE_ID_PARAMETER 				= "ROLE_ID";
	
	public static boolean IS_DYNAMIC_AUTH_CHECK			= true;										// 권한체크 동적모드여부(true 일 경우 URL이 호출될 때마다 DB를 조회하여 체크한다.)
	
	@Resource(name = "customAuthenticationProvider")
    private CustomAuthenticationProvider authProvider;

    /********* SVC 정의부분 시작 *********/
	@Resource(name = "customUserService")
    private CustomUserService customUserService; 
    /********* SVC 정의부분 끝 *********/
	
	@Bean
	public SecurityFilterChain filterChan(HttpSecurity http) throws Exception {
		
		// 1. 크로스 사이트 요청 위조(CSRF) 방지설정
		http.csrf().disable();
		// 2. 로그인처리 필터 필터체인에 삽입
		http.addFilterAt(customUsernamePasswordAuthenticationFilter(authManager(http)), UsernamePasswordAuthenticationFilter.class);
		// 3. URL별 권한설정
		this.setAntMatchers(http);
		http
		// 4. 로그인 설정
			.formLogin()
				.loginPage(LOGIN_GO_ACTION)	// 로그인 페이지 URL 지정
				.and()
		// 5. 로그아웃 설정	
			.logout()
				.logoutUrl(LOGOUT_ACTION) // 로그아웃 URL
	        	.logoutSuccessUrl(LOGOUT_SUCCS_ACTION) // 성공 리턴 URL
	        	.invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
	        	.deleteCookies("JSESSIONID", "remember-me") // JSESSIONID, remember-me 쿠키 삭제
			;
		
	    // 6. 접근제한처리 설정	
		http.exceptionHandling().accessDeniedHandler(acessDeniedHandler());
		// 7. 세션 설정
		SessionListener.USER_LOGIN_PRIVILEGE_KIND = SessionListener.USER_LOGIN_PRIVILEGE_KIND_LATER;

		return http.build();
	}

	@Bean
	public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager);
		customUsernamePasswordAuthenticationFilter.setFilterProcessesUrl(LOGIN_PROCESS_ACTION); // 로그인 처리 URL 지정
		customUsernamePasswordAuthenticationFilter.setUsernameParameter(USERNAME_PARAMETER); // USER_NAME 파라메터명 지정
		customUsernamePasswordAuthenticationFilter.setPasswordParameter(PASSWORD_PARAMETER); // PASSWORD 파라메터명 지정
		customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSucessHandler());  // 성공 handler
		customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler()); // 실패 handler
		return customUsernamePasswordAuthenticationFilter; 
	}
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception{
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(authProvider);
		AuthenticationManager authenticationManager = builder.build();
		return authenticationManager;
	}
	@Bean
	public CustomAuthSucessHandler customAuthSucessHandler() {
		return new CustomAuthSucessHandler();
	}
	@Bean
	public CustomAuthFailureHandler customAuthFailureHandler() {
		return new CustomAuthFailureHandler();
	}
	@Bean
	public CustomAccessDeniedHandler acessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

	protected void setAntMatchers(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authReq = http.authorizeRequests();
		/*** 기본설정-권한허용자원 ***/
		authReq.antMatchers(
				
			/*** 정적자원 ***/	
			// CSS	
			"/css/**"
			, "/**/*.css"
			, "/**/*.woff2"
			// IMAGE
			, "/images/**"
			, "/**/*.bmp"
			, "/**/*.jpg"
			, "/**/*.gif"
			, "/**/*.ico"
			, "/**/*.png"
			, "/**/*.tif"
			// JS
			, "/js/**"
			, "/**/*.js"
			// HTML
			, "/html/**"
			, "/**/*.html"
			, "/**/*.htm"

			/*** 동적자원중 권한체크가 필요없는 자원들 ***/	
			, "/defaultLink.do*"
			, "/test/**"
			, "/analyzer/**"
			, MAIN_PAGE
			, LOGIN_PAGE
			, LOGIN_GO_ACTION
			, LOGIN_PROCESS_ACTION
			, LOGIN_PROCESS_SUCCESS_ACTION
			, LOGIN_PROCESS_FAILURE_ACTION
			, LOGIN_CHECK_ACTION
			, LOGOUT_ACTION
			, LOGOUT_SUCCS_ACTION
			, ERROR_URL_PATTERN
			, KAKAO_LOGIN_PAGE
			, PROXY_ACTION
		).permitAll()
		.mvcMatchers("/", "/index.html") // antMatchers 는 슬래쉬(/)로 끝나는 경우 제대로 검증하지 못하므로 루트(/)는 mvcMatchers를 사용한다.
		.permitAll(); 
		
		/*** 기본설정-역할별URL DB설정(정적체크-Application기동시 한번만 전체설청 받아오는 경우) ***/
		if(!IS_DYNAMIC_AUTH_CHECK) {
			List<Map<String, Object>> list = customUserService.selectListUrlAndRole(null);
			HashMap<String, ArrayList<String>> urlRolesMap = new HashMap<String, ArrayList<String>>();
			for (Map<String, Object> m : list) {
				String url = m.get("PROG_URL").toString();
				String role = m.get("ROLE_ID").toString();
				if(!urlRolesMap.containsKey(url)) {
					urlRolesMap.put(url, new ArrayList<String>());
				}
				ArrayList<String> roleList = urlRolesMap.get(url);
				if(!roleList.contains(role)) {
					roleList.add(role);
				}
			}
			Iterator<String> keyUrl = urlRolesMap.keySet().iterator();
			while(keyUrl.hasNext()) {
				String url = keyUrl.next();
				ArrayList<String> roleList = urlRolesMap.get(url);
				String[] roles = new String[roleList.size()];
				roleList.toArray(roles);
				authReq.antMatchers(url).hasAnyAuthority(roles);
			}
			authReq.anyRequest().authenticated();
		/*** 기본설정-역할별URL DB설정(동적체크-URL호출시마다 DB체크하는 경우) ***/
		}else {
			authReq.anyRequest().access("@customAuthChecker.check(request, authentication)");
		}
	}
}
