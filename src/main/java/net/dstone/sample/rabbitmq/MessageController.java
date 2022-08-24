package net.dstone.sample.rabbitmq;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    Sender sender;

    @PostMapping("/messages")
    public String sendMessage(final @RequestBody Map<String,String> req ) {
        String message = req.get("message");
        try {
            sender.send(message);
            return "send message success";
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
            return "send message fail";
        }
    }
}
