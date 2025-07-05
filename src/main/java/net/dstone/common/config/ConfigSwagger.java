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
	
    /**
     * Swagger 제너레이팅 메소드
     * 제너레이팅 할 때 spring.security.enabled 를 false 로 해주고 제너레이팅. 
     * http://{localhost}:{포트}/swagger-ui/index.html 로 접근
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        	.select()
        	.apis(RequestHandlerSelectors.basePackage("net.dstone.sample"))
        	.paths(PathSelectors.any())
        	.build();
    }
    
    /******************************************************************************
    - 아래와 같이 메소드에 어노테이션을 붙여주어야 한다.
    @Api				Controller 설명
    @ApiOperation		메서드 설명
    @ApiParam			파라미터 설명
    @ApiModel			DTO 설명
    @ApiModelProperty	DTO 필드 설명
    
	- 샘플
    @Api(tags = "사용자 API")
	@RestController
	@RequestMapping("/user")
	public class UserController {
	
	    @ApiOperation(value = "회원 조회", notes = "회원 ID로 정보를 조회합니다.")
	    @GetMapping("/{id}")
	    public UserDto getUser(@ApiParam(value = "회원 ID", required = true) @PathVariable Long id) {
	        return userService.getUser(id);
	    }
	} 
    ******************************************************************************/
}