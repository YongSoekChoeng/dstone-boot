package net.dstone.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import net.dstone.common.core.BaseObject;

@EnableAsync
@Configuration
@Import({ 
	ConfigAspect.class,
	ConfigDatasource.class,
	ConfigEnc.class,
	ConfigListener.class,
	ConfigMapper.class,
	ConfigMq.class,
	ConfigProperty.class,
	ConfigSecurity.class,
	ConfigSwagger.class,
	ConfigTransaction.class,
	ConfigWebSocket.class
})
public class Config extends BaseObject{
	
}
