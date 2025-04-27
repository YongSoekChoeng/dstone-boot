package net.dstone.common;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import net.dstone.common.utils.StringUtil;

@SpringBootApplication
@ComponentScan(basePackages={"net.dstone"})
public class DstoneBootApplication extends SpringBootServletInitializer {

    /**
     * 외장 WAS에서 운용할 때를 위한 조치.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
    	SpringApplicationBuilder builder = applicationBuilder.sources(DstoneBootApplication.class);
        return builder;
    }
    
	public static void main(String[] args) {

	    setSecurity();
	    checkSecurity();
	    
		SpringApplication app = new SpringApplication(DstoneBootApplication.class);
		app.addListeners(new ApplicationPidFileWriter()); // ApplicationPidFileWriter 설정
	    app.run(args);
	}
	
	private static void setSecurity() {
		try {
			/******************************* 낮은버젼에서 SSL을 사용할 수 있도록 하기위한 조치 시작 *******************************/
			// JDK1.6-TLSv1사용/JDK1.7-TLSv1.1사용/JDK1.8이상-TLSv1.2사용.
			// 1. JDK1.8이상은 JAVA Security 에서 TLSv1, TLSv1.1를 사용불가 알고리즘으로 디폴트 세팅되어 있음. 따라서 이 시스템 프러퍼티를 수정.
			
			String disabledAlgorithms = "";
			ArrayList<String> delFromDisabledAlgorithmsList = new ArrayList<String>();
			delFromDisabledAlgorithmsList.add("TLSv1");
			delFromDisabledAlgorithmsList.add("TLSv1.1");
			
			// 1. jdk.certpath.disabledAlgorithms
			disabledAlgorithms = java.security.Security.getProperty("jdk.certpath.disabledAlgorithms");
			if( !StringUtil.isEmpty(disabledAlgorithms) ) {
				for(String delFromDisabledAlgorithm : delFromDisabledAlgorithmsList) {
					disabledAlgorithms = StringUtil.replace(disabledAlgorithms, delFromDisabledAlgorithm+",", "");
				}
				java.security.Security.setProperty("jdk.certpath.disabledAlgorithms", disabledAlgorithms);	
			}

			// 2. jdk.jar.disabledAlgorithms
			disabledAlgorithms = java.security.Security.getProperty("jdk.jar.disabledAlgorithms");
			if( !StringUtil.isEmpty(disabledAlgorithms) ) {
				for(String delFromDisabledAlgorithm : delFromDisabledAlgorithmsList) {
					disabledAlgorithms = StringUtil.replace(disabledAlgorithms, delFromDisabledAlgorithm+",", "");
				}
				java.security.Security.setProperty("jdk.jar.disabledAlgorithms", disabledAlgorithms);	
			}

			// 3. jdk.tls.disabledAlgorithms
			disabledAlgorithms = java.security.Security.getProperty("jdk.tls.disabledAlgorithms");
			if( !StringUtil.isEmpty(disabledAlgorithms) ) {
				for(String delFromDisabledAlgorithm : delFromDisabledAlgorithmsList) {
					disabledAlgorithms = StringUtil.replace(disabledAlgorithms, delFromDisabledAlgorithm+",", "");
				}
				java.security.Security.setProperty("jdk.tls.disabledAlgorithms", disabledAlgorithms);	
			}
			
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			System.setProperty("jsse.enableSNIExtension", "false");		
			/******************************* 낮은버젼에서 SSL을 사용할 수 있도록 하기위한 조치 끝 *********************************/
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	private static void checkSecurity() {
		try {
			StringBuffer msg = new StringBuffer();
			msg.append("/******************************* SSL/TLS 설정 체크 시작 *********************************/").append("\n");
			msg.append("https.protocols").append(":").append(System.getProperty("https.protocols", "")).append("\n");
			msg.append("jdk.tls.client.protocols").append(":").append(System.getProperty("jdk.tls.client.protocols", "")).append("\n");
			msg.append("jsse.enableSNIExtension").append(":").append(System.getProperty("jsse.enableSNIExtension", "")).append("\n");
			msg.append("jdk.certpath.disabledAlgorithms").append(":").append(java.security.Security.getProperty("jdk.certpath.disabledAlgorithms")).append("\n");
			msg.append("jdk.jar.disabledAlgorithms").append(":").append(java.security.Security.getProperty("jdk.jar.disabledAlgorithms")).append("\n");
			msg.append("jdk.tls.disabledAlgorithms").append(":").append(java.security.Security.getProperty("jdk.tls.disabledAlgorithms")).append("\n");
			msg.append("/******************************* SSL/TLS 설정 체크 끝 *********************************/").append("\n");
			
			System.out.println(msg.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
