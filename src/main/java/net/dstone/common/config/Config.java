package net.dstone.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	ConfigAspect.class,
	ConfigDatasource.class,
	ConfigEnc.class,
	ConfigListener.class,
	ConfigMapper.class,
	ConfigProperty.class,
	ConfigSecurity.class,
	ConfigTransaction.class
})
public class Config{
	
}
