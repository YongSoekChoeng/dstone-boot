package net.dstone.sample.kakao.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.dstone.common.config.ConfigProperty;
import net.dstone.common.utils.StringUtil;
import net.dstone.sample.kakao.service.KakaoService;

@Controller
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoLoginController extends net.dstone.common.biz.BaseController { 

    @Autowired 
    private KakaoService kakaoService;

    @RequestMapping("/loginPage.do")
    @ConfigurationProperties("interface.kakao")
    public String loginPage(Model model) {
    	
    	String clientId = ConfigProperty.getProperty("interface.kakao.client-id");
    	String loginUri = ConfigProperty.getProperty("interface.kakao.login-url");
    	String loginRedirectUri = ConfigProperty.getProperty("interface.kakao.login-redirect-uri");
    	loginUri = StringUtil.replace(loginUri, "@client_id@" , clientId);
    	loginUri = StringUtil.replace(loginUri, "@redirect_uri@" , loginRedirectUri);
    	
    	info("clientId["+clientId+"]" + "loginUri["+loginUri+"]");
    	
        String location = loginUri;
        model.addAttribute("location", location);
        
        return "/sample/kakao/login";
    }
    
    @RequestMapping("/loginCallback.do")
    public String loginCallback(@RequestParam("code") String code, HttpServletRequest request) {
    	
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        request.getSession().setAttribute("accessToken", accessToken);

        java.util.Map<String, String> userInfo = kakaoService.getUserInfo(accessToken);
        request.setAttribute("userInfo", userInfo);

        // User 로그인, 또는 회원가입 로직 추가
        
        return "/sample/kakao/loginCallback";
    }
    
    @RequestMapping("/logout.do")
    public void logout(Model model, HttpServletRequest request, HttpServletResponse response) {
    	
    	String clientId = ConfigProperty.getProperty("interface.kakao.client-id");
    	String logoutUri = ConfigProperty.getProperty("interface.kakao.logout-url");
    	String logoutRedirectUri = ConfigProperty.getProperty("interface.kakao.logout-redirect-uri");
    	logoutUri = StringUtil.replace(logoutUri, "@client_id@" , clientId);
    	logoutUri = StringUtil.replace(logoutUri, "@logout_redirect_uri@" , logoutRedirectUri);
    	
    	info("clientId["+clientId+"]" + "loginUri["+logoutUri+"]");

    	try {
            String location = logoutUri;
            response.sendRedirect(location);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return ;
    }
    
    @RequestMapping("/logoutCallback.do")
    public String logoutCallback(Model model, HttpServletRequest request, HttpServletResponse response) {

    	String clientId = ConfigProperty.getProperty("interface.kakao.client-id");
    	String loginUri = ConfigProperty.getProperty("interface.kakao.login-url");
    	String loginRedirectUri = ConfigProperty.getProperty("interface.kakao.login-redirect-uri");
    	loginUri = StringUtil.replace(loginUri, "@client_id@" , clientId);
    	loginUri = StringUtil.replace(loginUri, "@redirect_uri@" , loginRedirectUri);
    	
    	info("clientId["+clientId+"]" + "loginUri["+loginUri+"]");
    	
        String location = loginUri;
        model.addAttribute("location", location);
        
        return "/sample/kakao/login";
        
    }
}
