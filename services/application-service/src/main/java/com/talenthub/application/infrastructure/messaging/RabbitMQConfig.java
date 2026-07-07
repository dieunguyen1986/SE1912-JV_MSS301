package com.talenthub.application.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Tên exchange: convention theo domain (talenthub) + purpose (events)
    public static final String EXCHANGE_NAME = "talenthub.events";

    // Tên queue: convention theo consumer service + event type
    public static final String QUEUE_NOTIFICATION = "notification.application-created";

    // Routing key: convention theo aggregate + action
    public static final String ROUTING_KEY_APP_CREATED = "application.created";

    @Bean
    public TopicExchange talenthubExchange() {
        return new TopicExchange(EXCHANGE_NAME, /*durable*/ true, /*autoDelete*/ false);
    }

    /**
     * Queue chính: durable = true để message không bị mất khi RabbitMQ restart.
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable(QUEUE_NOTIFICATION)
                .build();
    }

    /**
     * Binding: gắn queue vào exchange với routing key cụ thể.
     * Khi producer gửi message với routing key "application.created",
     * RabbitMQ sẽ route message đó vào queue "notification.application-created".
     */
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange talenthubExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(talenthubExchange)
                .with(ROUTING_KEY_APP_CREATED);
    }

    /**
     * MessageConverter: chuyển đổi Java object sang JSON khi gửi message,
     * và JSON sang Java object khi nhận message.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }
}