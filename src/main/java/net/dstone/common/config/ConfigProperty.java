package net.dstone.common.config;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import net.dstone.common.utils.LogUtil;

@Component("configProperty")
@PropertySources({
    @PropertySource("classpath:env.properties")
})
public class ConfigProperty { 

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigProperty.class);

	/**
	 * env는 기본적으로 application.yml의 프로퍼티정보를 로딩한다. @PropertySource 가 세팅되어 있으면 해당 프로퍼티의 정보도 시스템프로퍼티로 로딩한다. 
	 * 이 경우 application.yml에서 시스템프로퍼티를 ${프로퍼티} 형식으로 사용할 수 있다.
	 */
	@Autowired 
	Environment env;
	
	public String getProperty(String key) {
		String val = env.getProperty(key);
		return val;
	}
	
}

