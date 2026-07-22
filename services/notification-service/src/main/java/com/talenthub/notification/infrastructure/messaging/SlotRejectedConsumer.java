package com.talenthub.notification.infrastructure.messaging;

import com.talenthub.events.JobSlotRejectedEvent;
import com.talenthub.notification.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Case 2: Lắng nghe event "job.slot.rejected" từ job-service.
 * Khi applicantCount > maxApplicants => Số lượng ứng viên đã đạt tối đa (hết
 * slot).
 * => Gửi email thông báo đơn ứng tuyển không thể tiếp nhận.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SlotRejectedConsumer {

        private final EmailService emailService;

        @RabbitListener(bindings = @QueueBinding(value = @Queue(name = RabbitMQConfig.QUEUE_NOTIFICATION_SLOT_REJECTED, durable = "true"), exchange = @Exchange(value = RabbitMQConfig.EXCHANGE_NAME, type = "topic"), key = RabbitMQConfig.RK_JOB_SLOT_REJECTED))
        public void handleSlotRejected(JobSlotRejectedEvent event) {
                log.info("Received job.slot.rejected: applicationId={}, jobId={}, reason={}",
                                event.applicationId(), event.jobId(), event.reason());

                emailService.sendSlotFullRejection(
                                event.candidateEmail(),
                                event.candidateFullName(),
                                event.reason());

                log.info("Sent slot-rejected email to {} for applicationId={}",
                                event.candidateEmail(), event.applicationId());
        }
}
