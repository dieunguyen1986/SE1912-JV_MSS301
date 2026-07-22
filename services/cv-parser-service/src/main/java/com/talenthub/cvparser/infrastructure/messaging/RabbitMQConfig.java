package com.talenthub.cvparser.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "talenthub.events";
    
    public static final String QUEUE_CV_JOB_SLOT_RESERVED = "cv.job.slot.reserved.queue";
    public static final String RK_JOB_SLOT_RESERVED = "job.slot.reserved";
    public static final String RK_CV_PARSED_SUCCESS = "cv.parsed.success";
    public static final String RK_CV_PARSED_FAILED = "cv.parsed.failed";

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
