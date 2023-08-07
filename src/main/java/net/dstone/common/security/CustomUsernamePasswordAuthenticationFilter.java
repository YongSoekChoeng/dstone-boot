package net.dstone.common.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private net.dstone.common.utils.RequestUtil requestUtil;
	
	public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			requestUtil = new net.dstone.common.utils.RequestUtil(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.attemptAuthentication(request, response);
	}
	
	@Override
	public String obtainUsername(HttpServletRequest request) {
		return this.getParamFromDataRequest(request, this.getUsernameParameter());
	}

	@Override
	public String obtainPassword(HttpServletRequest request) {
		return this.getParamFromDataRequest(request, this.getPasswordParameter());
	}
	
	public String getParamFromDataRequest(HttpServletRequest request, String paramName){
		String paramVal = null;
		paramVal = request.getParameter(paramName);
		return paramVal;
	}

}
