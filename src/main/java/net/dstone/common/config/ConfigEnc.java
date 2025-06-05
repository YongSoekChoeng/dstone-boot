package net.dstone.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import net.dstone.common.utils.LogUtil;

@EnableEncryptableProperties
public class ConfigEnc {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigEnc.class);

	// 1-1. 암복호화 설정.
	@Bean("jasyptStringEncryptor") 
	public StringEncryptor stringEncryptor() { 
        return net.dstone.common.utils.EncUtil.getEncryptor();
	} 
}
