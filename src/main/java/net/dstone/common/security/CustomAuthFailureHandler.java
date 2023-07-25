package net.dstone.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import net.dstone.common.config.ConfigSecurity;
import net.dstone.common.conts.ErrCd;
import net.dstone.common.exception.SecException;
import net.dstone.common.utils.LogUtil;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final LogUtil logger = new LogUtil(CustomAuthFailureHandler.class);
	
	public CustomAuthFailureHandler() {
	}

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SecException secException = null;
        if(exception instanceof SecException){
        	secException = (SecException)exception;
        }else {
        	secException = new SecException(ErrCd.SYS_ERR);
        }
        request.setAttribute("SecException", secException);
        super.setUseForward(true);
        super.setDefaultFailureUrl(ConfigSecurity.LOGIN_PROCESS_FAILURE_ACTION);
        super.onAuthenticationFailure(request, response, exception);
    }
}