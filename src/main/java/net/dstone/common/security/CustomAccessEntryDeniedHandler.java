package net.dstone.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import net.dstone.common.config.ConfigSecurity;
import net.dstone.common.core.BaseObject;

public class CustomAccessEntryDeniedHandler extends BaseObject implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		String requestUri = request.getRequestURI();
    	this.debug(this.getClass().getName() + ".commence(requestUri["+requestUri+"]) =================>>>> has been called !!!");
    	//request.getRequestDispatcher(ConfigSecurity.LOGIN_GO_ACTION).forward(request, response);
    	response.sendRedirect(ConfigSecurity.LOGIN_GO_ACTION);
	}
    
}