package net.dstone.common.mq.rabbitmq.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import net.dstone.common.biz.BaseService;

@Service
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true")
public class ConsumerService extends BaseService {

    @RabbitListener(queues = "app.notifications.queue")
    public void receiveMessageFromNotifications(String msg) {
        this.info("app.notifications.queue 로부터 받은메세지[" + msg + "]");
    }

    @RabbitListener(queues = "app.orders.queue")
    public void receiveMessageFromOrders(String msg) {
        this.info("app.orders.queue 로부터 받은메세지[" + msg + "]");
    }
    
}
