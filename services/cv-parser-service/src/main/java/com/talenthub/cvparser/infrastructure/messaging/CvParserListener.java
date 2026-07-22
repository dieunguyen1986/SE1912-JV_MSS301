package com.talenthub.cvparser.infrastructure.messaging;

import com.talenthub.events.CVParseFailedEvent;
import com.talenthub.events.CVParsedEvent;
import com.talenthub.events.JobSlotReservedEvent;
import com.talenthub.cvparser.service.MockCvParserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CvParserListener {

    private final MockCvParserService cvParsingService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_CV_JOB_SLOT_RESERVED, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_JOB_SLOT_RESERVED
    ))
    public void onJobSlotReserved(JobSlotReservedEvent event) {
        log.info("Received JobSlotReservedEvent for applicationId: {}, candidateId: {}", event.applicationId(), event.candidateId());

        try {
            // T3: Parse CV
            cvParsingService.processCvParsing(event.candidateId(), event.cvFileUrl());
            
            // Success
            CVParsedEvent successEvent = new CVParsedEvent(event.applicationId(), event.candidateEmail(), event.candidateFullName());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_CV_PARSED_SUCCESS, successEvent);
            log.info("Successfully published CVParsedEvent for applicationId: {}", event.applicationId());
            
        } catch (Exception e) {
            // FAILURE: Publish error event to trigger Compensation
            CVParseFailedEvent failedEvent = new CVParseFailedEvent(
                event.applicationId(), 
                event.jobId(),
                event.candidateEmail(),
                event.candidateFullName(),
                "Lỗi phân tích file: " + e.getMessage()
            );
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_CV_PARSED_FAILED, failedEvent);
            log.error("Published CVParseFailedEvent for applicationId: {}. Reason: {}", event.applicationId(), e.getMessage());
        }
    }
}
