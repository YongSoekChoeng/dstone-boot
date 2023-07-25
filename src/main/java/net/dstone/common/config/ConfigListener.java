package net.dstone.common.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.dstone.common.utils.LogUtil;
import net.dstone.common.web.SessionListener;

@Configuration
public class ConfigListener {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigListener.class);

	@Bean
	public ServletListenerRegistrationBean<SessionListener> getSessionListener() {
		ServletListenerRegistrationBean<SessionListener> listener = new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
		return listener;
	}
}
