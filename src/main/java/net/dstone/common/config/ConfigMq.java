package net.dstone.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import net.dstone.common.core.BaseObject;

@Component
public class ConfigMq extends BaseObject {

	@Autowired
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

	/***************************** Rabbit MQ 설정 시작 *****************************/
	
	/*** 바인딩 갯수만큼 세팅 시작 ***/
	// binding-main
    /** 1. Exchange 구성합니다. */
    @Bean
    public DirectExchange directExchangeMain() {
        return new DirectExchange(configProperty.getProperty("spring.rabbitmq.bindings.binding-main.exchange-id"));
    }
    /** 2. 큐를 구성합니다. */
    @Bean
    public Queue queueRabbitMain() {
        return new Queue(configProperty.getProperty("spring.rabbitmq.bindings.binding-main.queue-id"), false);
    }
    /** 3. 큐와 DirectExchange를 바인딩합니다. */
    @Bean
    public Binding bindingMain() {
        return BindingBuilder.bind(queueRabbitMain()).to(directExchangeMain()).with(configProperty.getProperty("spring.rabbitmq.bindings.binding-main.queue-id"));
    }
    // binding-sub
    /** 1. Exchange 구성합니다. */
    @Bean
    public DirectExchange directExchangeSub() {
        return new DirectExchange(configProperty.getProperty("spring.rabbitmq.bindings.binding-sub.exchange-id"));
    }
    /** 2. 큐를 구성합니다. */
    @Bean
    public Queue queueRabbitSub() {
        return new Queue(configProperty.getProperty("spring.rabbitmq.bindings.binding-sub.queue-id"), false);
    }
    /** 3. 큐와 DirectExchange를 바인딩합니다. */
    @Bean
    public Binding bindingSub() {
        return BindingBuilder.bind(queueRabbitSub()).to(directExchangeSub()).with(configProperty.getProperty("spring.rabbitmq.bindings.binding-sub.queue-id"));
    }

    /*** 바인딩 갯수만큼 세팅 끝 ***/


    /**
     * 4. RabbitMQ와의 연결을 위한 ConnectionFactory을 구성합니다.
     * Application.properties의 RabbitMQ의 사용자 정보를 가져와서 RabbitMQ와의 연결에 필요한 ConnectionFactory를 구성합니다.
     *
     * @return ConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(configProperty.getProperty("spring.rabbitmq.host"));
        connectionFactory.setPort(Integer.parseInt(configProperty.getProperty("spring.rabbitmq.port")));   
        connectionFactory.setUsername(configProperty.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(configProperty.getProperty("spring.rabbitmq.password"));
        connectionFactory.setVirtualHost(configProperty.getProperty("spring.rabbitmq.virtual-host"));
        return connectionFactory;
    }

    /**
     * 5. 메시지를 전송하고 수신하기 위한 JSON 타입으로 메시지를 변경합니다.
     * Jackson2JsonMessageConverter를 사용하여 메시지 변환을 수행합니다. JSON 형식으로 메시지를 전송하고 수신할 수 있습니다
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 6. 구성한 ConnectionFactory, MessageConverter를 통해 템플릿을 구성합니다.
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory());
        rabbitTemplate.setMessageConverter(this.messageConverter());
        return rabbitTemplate;
    }
	/***************************** Rabbit MQ 설정 끝 *****************************/

	/***************************** Radis 설정 시작 *****************************/

	/***************************** Radis 설정 끝 *****************************/

}
