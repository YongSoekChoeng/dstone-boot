package net.dstone.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.StringUtil;
import net.dstone.common.websocket.controller.WebSocketController;
import net.dstone.common.websocket.handler.BaseTextWebSocketHandler;

@Controller
@EnableWebSocket
@EnableWebSocketMessageBroker
public class ConfigWebSocket implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

	private static LogUtil logger = new LogUtil(ConfigWebSocket.class);
	
	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

	/***************************** WebSocketConfigurer 설정 시작 *****************************/
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new BaseTextWebSocketHandler(), "/textHandler")
                .setAllowedOrigins("*")
                .withSockJS();
    }
	/***************************** WebSocketConfigurer 설정 끝 *****************************/

	/***************************** WebSocketMessageBrokerConfigurer 설정 시작 *****************************/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	String url = "";
    	String protocol = "http";
    	if( !StringUtil.isEmpty(configProperty.getProperty("server.ssl.enabled")) ) {
    		if("true".equals(configProperty.getProperty("server.ssl.enabled"))) {
    			protocol = "https";
    		}
    	}
    	String port = configProperty.getProperty("server.port");
    	String contextPath = configProperty.getProperty("server.servlet.context-path");
    	url = protocol + "://localhost:" + port;
    	if( !StringUtil.isEmpty(contextPath) && !"/".equals(contextPath) ) {
    		url = url + contextPath;
    	}
    	url = url + "/*";
logger.sysout("url================>>>" + url);
        /*
         * addEndpoint : 클라이언트가 WebSocket에 연결하기 위한 엔드포인트를 "/ws-stomp"로 설정합니다.
         * withSockJS : WebSocket을 지원하지 않는 브라우저에서도 SockJS를 통해 WebSocket 기능을 사용할 수 있게 합니다.
         */
        registry
        // 클라이언트가 WebSocket에 연결하기 위한 엔드포인트를 "/ws-stomp"로 설정합니다.
        .addEndpoint("/ws-stomp")
        // 클라이언트의 origin을 명시적으로 지정
        .setAllowedOrigins("*")
        // WebSocket을 지원하지 않는 브라우저에서도 SockJS를 통해 WebSocket 기능을 사용할 수 있게 합니다.
        .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	
        // 구독(sub) : 접두사로 시작하는 메시지를 브로커가 처리하도록 설정합니다. 클라이언트는 이 접두사로 시작하는 주제를 구독하여 메시지를 받을 수 있습니다.
        // 예를 들어, 소켓 통신에서 사용자가 특정 메시지를 받기위해 "/sub"이라는 prefix 기반 메시지 수신을 위해 Subscribe합니다.
        config.enableSimpleBroker("/sub");

        // 발행(pub) : 접두사로 시작하는 메시지는 @MessageMapping이 달린 메서드로 라우팅됩니다. 클라이언트가 서버로 메시지를 보낼 때 이 접두사를 사용합니다.
        // 예를 들어, 소켓 통신에서 사용자가 특정 메시지를 전송하기 위해 "/pub"라는 prefix 기반 메시지 전송을 위해 Publish 합니다.
        config.setApplicationDestinationPrefixes("/pub");

    }
	/***************************** WebSocketMessageBrokerConfigurer 설정 끝 *****************************/

}
