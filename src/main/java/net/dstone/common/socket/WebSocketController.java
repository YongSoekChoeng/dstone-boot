package net.dstone.common.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import net.dstone.common.biz.BaseController;

public class WebSocketController extends BaseController {

    @SendTo("/topic/messages")
    @MessageMapping("/chat.send")
    public String sendMessage(String message) {
        return message;
    }
}
