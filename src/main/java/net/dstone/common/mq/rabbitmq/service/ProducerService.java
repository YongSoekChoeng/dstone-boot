package net.dstone.common.mq.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import net.dstone.common.biz.BaseService;
import net.dstone.common.utils.DataSet;

@Service
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true")
public class ProducerService extends BaseService {

	@Autowired
	RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchangeId, String routingKey, DataSet messageDs) {
        try {
            rabbitTemplate.convertAndSend(exchangeId, routingKey, messageDs.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
