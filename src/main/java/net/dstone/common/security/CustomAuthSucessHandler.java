package net.dstone.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import net.dstone.common.config.ConfigSecurity;
import net.dstone.common.exception.SecException;
import net.dstone.common.security.vo.CustomUserDetails;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.web.SessionListener;

@Component
public class CustomAuthSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final LogUtil logger = new LogUtil(CustomAuthSucessHandler.class);
	
	public CustomAuthSucessHandler() {
	}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, SecException {
        logger.info(this.getClass().getName() + ".onAuthenticationSuccess() =================>>>> has been called !!!");
        
    	// Do Something here !
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
        logger.info("Welcome login_success! session.getId() : " + request.getSession().getId() + " userDetails.getUsername():" + userDetails.getUsername() + " userDetails.getPassword():" + userDetails.getPassword());
        
        // 중복로그인 방지(먼저 로그인 한 세션을 삭제)
        SessionListener.getSessionidCheck(userDetails.getUsername());
        // 세션저장
        request.getSession().setAttribute(SessionListener.USER_LOGIN_SESSION_KEY, userDetails);
        
        setDefaultTargetUrl(ConfigSecurity.LOGIN_PROCESS_SUCCESS_ACTION);
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
}
