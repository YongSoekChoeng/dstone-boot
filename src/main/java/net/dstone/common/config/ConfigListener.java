package net.dstone.common.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import net.dstone.common.utils.LogUtil;
import net.dstone.common.web.SessionListener;

@Component
public class ConfigListener {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigListener.class);

	@Bean
	public ServletListenerRegistrationBean<SessionListener> getSessionListener() {
		ServletListenerRegistrationBean<SessionListener> listener = new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
		return listener;
	}
}
