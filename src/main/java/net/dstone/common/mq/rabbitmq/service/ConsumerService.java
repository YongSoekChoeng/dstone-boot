package net.dstone.common.mq.rabbitmq.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import net.dstone.common.biz.BaseService;

@Service
public class ConsumerService extends BaseService {

    @RabbitListener(queues = "notifications")
    public void receiveMessage(String msg) {
        this.info("받은메세지[" + msg + "]");
    }
    
}
