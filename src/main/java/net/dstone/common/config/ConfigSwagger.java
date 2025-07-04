package net.dstone.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnProperty(name = "spring.swagger-gen.enabled", havingValue = "true" )
@EnableSwagger2
public class ConfigSwagger {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        	.select()
        	.apis(RequestHandlerSelectors.basePackage("net.dstone.sample"))
        	.paths(PathSelectors.any())
        	.build();
    }
}