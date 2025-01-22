package net.dstone.sample.kakao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import net.dstone.common.config.ConfigProperty;
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
    	String redirectUri = ConfigProperty.getProperty("interface.kakao.redirect-uri");
    	
    	info("clientId["+clientId+"]" + "redirectUri["+redirectUri+"]");
    	
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
        model.addAttribute("location", location);
        return "/sample/kakao/login";
    }
    
    @RequestMapping("/loginCallback.do")
    public String callback(@RequestParam("code") String code, HttpServletRequest request) {
    	
    	String serverUrl = request.getScheme() + "://" +  "localhost"  + ":" + request.getLocalPort();
    	info( "serverUrl =====>>>" + serverUrl );
    	
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        kakaoService.getUserInfo(accessToken);

        // User 로그인, 또는 회원가입 로직 추가
        
        
        return "/sample/kakao/loginCallback";
    }
}
