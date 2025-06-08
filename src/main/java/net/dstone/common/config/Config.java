package net.dstone.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@Import({ 
	ConfigAspect.class,
	ConfigDatasource.class,
	ConfigEnc.class,
	ConfigListener.class,
	ConfigMapper.class,
	ConfigProperty.class,
	ConfigSecurity.class,
	ConfigTransaction.class,
	ConfigWebSocket.class
})
public class Config{
	
}
