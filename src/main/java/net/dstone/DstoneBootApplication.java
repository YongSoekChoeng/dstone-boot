package net.dstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DstoneBootApplication {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DstoneBootApplication.class);
		app.addListeners(new ApplicationPidFileWriter()); // ApplicationPidFileWriter 설정
	    app.run(args);
	}
	
}
