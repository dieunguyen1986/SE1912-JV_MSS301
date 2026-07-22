package com.talenthub.notification.infrastructure.messaging;

import com.talenthub.events.JobSlotReservedEvent;
import com.talenthub.notification.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationCreatedConsumer {

    private final EmailService emailService;

    /**
     * @RabbitListener: đánh dấu method này là consumer cho queue chỉ định.
     * Spring AMQP tự động tạo consumer thread và poll message liên tục.
     * <p>
     * Parameter "event" được Jackson tự động deserialize từ JSON body của message.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_JOB_SLOT_RESERVED)
    public void handle(JobSlotReservedEvent event) {
        log.info("Received ApplicationCreatedEvent: applicationId={}, candidate={}"
                , event.applicationId(), event.candidateEmail());

        // Gửi email auto-reply cho candidate
        emailService.sendAutoReply(
                event.candidateEmail(),
                event.candidateFullName(),
                event.jobTitle()
        );

        log.info("Processed ApplicationCreatedEvent successfully");
    }
}