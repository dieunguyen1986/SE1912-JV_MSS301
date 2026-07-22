package com.talenthub.notification.infrastructure.messaging;

import com.talenthub.events.CVParsedEvent;
import com.talenthub.notification.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Lắng nghe event "cv.parsed.success" từ cv-parser-service.
 * Khi CV được phân tích thành công -> gửi email chúc mừng / thông báo vòng tiếp theo.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CvParseSuccessConsumer {

    private final EmailService emailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_NOTIFICATION_CV_PARSE_SUCCESS, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_CV_PARSED_SUCCESS))
    public void handleCvParseSuccess(CVParsedEvent event) {
        log.info("Received cv.parsed.success: applicationId={}", event.applicationId());

        emailService.sendCvSuccessNotification(
                event.candidateEmail(),
                event.candidateFullName()
        );

        log.info("Sent CV success email to {} for applicationId={}",
                event.candidateEmail(), event.applicationId());
    }
}
