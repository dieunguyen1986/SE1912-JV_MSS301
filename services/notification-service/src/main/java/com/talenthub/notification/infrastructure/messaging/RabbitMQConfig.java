package com.talenthub.notification.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "talenthub.events";
    public static final String QUEUE_NOTIFICATION = "notification.application-created";
    public static final String ROUTING_KEY_APP_CREATED = "application.created";

    public static final String DLQ_EXCHANGE = "talenthub.events.dlx";
    public static final String DLQ_QUEUE = "notification.application-created.dlq";

    @Bean
    public TopicExchange talenthubExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable(QUEUE_NOTIFICATION)
                .deadLetterExchange(DLQ_EXCHANGE)   // message reject sẽ route vào DLX
                .build();
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange talenthubExchange) {
        return BindingBuilder.bind(notificationQueue).to(talenthubExchange).with(ROUTING_KEY_APP_CREATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }

    // ===== Dead Letter Infrastructure =====
    @Bean
    public FanoutExchange deadLetterExchange() {
        // Fanout vì DLX không cần routing: mọi message reject đều vào DLQ
        return new FanoutExchange(DLQ_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }


}