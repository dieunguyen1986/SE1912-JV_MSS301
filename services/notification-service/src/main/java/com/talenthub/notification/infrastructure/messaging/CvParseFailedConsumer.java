package com.talenthub.notification.infrastructure.messaging;

import com.talenthub.events.CVParseFailedEvent;
import com.talenthub.notification.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Case 3: Lắng nghe event "cv.parsed.failed" từ cv-parser-service.
 * Khi CV không match (parse thất bại): gửi email thông báo CV không phù hợp.
 * Đây là bước compensation trong Saga: candidate được thông báo rằng đơn bị huỷ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CvParseFailedConsumer {

    private final EmailService emailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_NOTIFICATION_CV_PARSE_FAILED, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_CV_PARSED_FAILED))
    public void handleCvParseFailed(CVParseFailedEvent event) {
        log.info("Received cv.parsed.failed: applicationId={}, jobId={}, reason={}",
                event.applicationId(), event.jobId(), event.reason());

        emailService.sendCvNotMatchNotification(
                event.candidateEmail(),
                event.candidateFullName(),
                event.reason()
        );

        log.info("Sent CV-not-match email to {} for applicationId={}",
                event.candidateEmail(), event.applicationId());
    }
}
