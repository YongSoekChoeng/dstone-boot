package net.dstone.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.stereotype.Component;

@Component
public class EncUtil {
	private static final String ENC_KEY = "jysn007db2admin";
	
	public static StringEncryptor getEncryptor() { 
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(ENC_KEY); // 암호화할 때 사용하는 키
        
//        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘(DES 방식, 양방향)
//        config.setProviderName("SunJCE");        
        
//        config.setAlgorithm("PBEWITHHMACSHA256ANDAES_128"); // 암호화 알고리즘(SHA256, 단방향)
        
        config.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC"); // 암호화 알고리즘(SHA256, 양방향)
        config.setProvider(new BouncyCastleProvider());
        
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
        config.setPoolSize("2"); // 인스턴스 pool
        config.setStringOutputType("base64"); //인코딩 방식
        encryptor.setConfig(config);
        return encryptor;
	}
	
	public static String encrypt(String plainStr) {
		return EncUtil.getEncryptor().encrypt(plainStr);
	}

	public static String decrypt(String encStr) {
		return EncUtil.getEncryptor().decrypt(encStr);
	}
	
}
