package net.dstone.common.websocket.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.dstone.common.utils.BeanUtil;
import net.dstone.common.utils.LogUtil;

public class BaseTextWebSocketHandler extends org.springframework.web.socket.handler.TextWebSocketHandler {

	private static LogUtil logger = new LogUtil(BaseTextWebSocketHandler.class);
	
    // WebSocket Session들을 관리하는 리스트입니다.
    private static final ConcurrentHashMap<String, WebSocketSession> clientSession = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("[+] handleTextMessage :: " + session);
        logger.info("[+] handleTextMessage :: " + message.getPayload());

        clientSession.forEach((key, value) -> {
            logger.info("key :: " + key + "  value :: " + value);
            if (!key.equals(session.getId())) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    value.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결 설정 후 로직
    	logger.info("[+] afterConnectionEstablished :: " + session.getId());
        clientSession.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 종료 후 로직
        clientSession.remove(session);
        logger.info("[+] afterConnectionClosed - Session: " + session.getId() + ", CloseStatus: " + status);
    }
}
