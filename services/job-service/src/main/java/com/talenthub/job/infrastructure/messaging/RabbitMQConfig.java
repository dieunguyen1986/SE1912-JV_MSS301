package com.talenthub.job.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "talenthub.events";
    public static final String QUEUE_JOB = "job.application-applied";
    public static final String RK_JOB_APPLIED_INCREMENT = "job.application-increment";
    public static final String RK_JOB_SLOT_RESERVED = "job.slot.reserved";
    public static final String RK_JOB_SLOT_REJECTED = "job.slot.rejected";
    
    public static final String RK_CV_PARSED_FAILED = "cv.parsed.failed";
    public static final String QUEUE_JOB_CV_PARSED_FAILED = "job.cv.parsed.failed.queue";
    
    // Tên queue: convention theo consumer service + event type
    public static final String QUEUE_NOTIFICATION = "notification.application-created";


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
