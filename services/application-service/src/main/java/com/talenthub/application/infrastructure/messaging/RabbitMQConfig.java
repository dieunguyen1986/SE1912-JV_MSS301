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
    public static final String RK_JOB_APPLIED_INCREMENT = "job.application-increment";
    public static final String RK_JOB_SLOT_REJECTED = "job.slot.rejected";
    public static final String QUEUE_JOB_SLOT_REJECTED = "application.job.slot.rejected.queue";
    
    public static final String RK_CV_PARSED_SUCCESS = "cv.parsed.success";
    public static final String QUEUE_APPLICATION_CV_PARSED_SUCCESS = "application.cv.parsed.success.queue";
    
    public static final String RK_CV_PARSED_FAILED = "cv.parsed.failed";
    public static final String QUEUE_APPLICATION_CV_PARSED_FAILED = "application.cv.parsed.failed.queue";

    // Dead Letter Exchange: phải khớp với notification-service
    public static final String DLQ_EXCHANGE = "talenthub.events.dlx";


    @Bean
    public TopicExchange talenthubExchange() {
        return new TopicExchange(EXCHANGE_NAME, /*durable*/ true, /*autoDelete*/ false);
    }

    /**
     * Queue chính: durable = true để message không bị mất khi RabbitMQ restart.
     * deadLetterExchange: phải khớp với cấu hình của notification-service,
     * nếu không RabbitMQ sẽ báo PRECONDITION_FAILED khi redeclare queue.
     */
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable(QUEUE_NOTIFICATION)
                .deadLetterExchange(DLQ_EXCHANGE)
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
     * Queue để application-service nhận event job.slot.rejected từ job-service.
     */
    @Bean
    public Queue jobSlotRejectedQueue() {
        return QueueBuilder.durable(QUEUE_JOB_SLOT_REJECTED).build();
    }

    @Bean
    public Binding jobSlotRejectedBinding(Queue jobSlotRejectedQueue, TopicExchange talenthubExchange) {
        return BindingBuilder
                .bind(jobSlotRejectedQueue)
                .to(talenthubExchange)
                .with(RK_JOB_SLOT_REJECTED);
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