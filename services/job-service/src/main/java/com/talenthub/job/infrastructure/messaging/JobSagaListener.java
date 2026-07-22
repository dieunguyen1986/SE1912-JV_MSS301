package com.talenthub.job.infrastructure.messaging;

import com.talenthub.events.CVParseFailedEvent;
import com.talenthub.events.JobSlotRejectedEvent;
import com.talenthub.events.JobSlotReservedEvent;
import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobSagaListener {
    private final JobRepository jobRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = RabbitMQConfig.QUEUE_JOB, durable = "true"), exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic"), key = RabbitMQConfig.RK_JOB_APPLIED_INCREMENT))
    @Transactional
    public void increaseCountApplicant(JobSlotReservedEvent event) {
        Optional<JobAggregate> optional = jobRepository.findById(event.jobId());

        JobAggregate job = optional.orElseThrow(() -> {
            throw new RuntimeException("Job not found id = " + event.jobId());
        });

        if (job.getApplicantCount() < job.getMaxApplicants()) {
            job.incrementApplicantCount();
            jobRepository.save(job);

            log.info("Incremented applicant count for jobId={}, new count={}", event.jobId(), job.getApplicantCount());

            // publish message to reserved routing key
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_JOB_SLOT_RESERVED, event);
            } catch (Exception e) {
                log.error("Failed to publish JobSlotReservedEvent", e);
                throw new RuntimeException("Can not publish message to the consumers", e);
            }
        } else {
            log.warn("Job is full or closed. Cannot accept new application for jobId={}", event.jobId());

            // publish fail apply - JobSlotRejectedEvent
            JobSlotRejectedEvent rejectedEvent = new JobSlotRejectedEvent(
                    event.applicationId(),
                    event.jobId(),
                    event.candidateId(),
                    event.candidateEmail(),
                    event.candidateFullName(),
                    "Hết slot ứng tuyển hoặc Job đã đóng");
            try {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_JOB_SLOT_REJECTED,
                        rejectedEvent);
            } catch (Exception e) {
                log.error("Failed to publish JobSlotRejectedEvent", e);
                throw new RuntimeException("Can not publish message to the consumers", e);
            }
        }
    }

    /**
     * Compensation: CV parse thất bại, decrement applicant count (hoàn trả slot đã
     * reserve).
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = RabbitMQConfig.QUEUE_JOB_CV_PARSED_FAILED, durable = "true"), exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic"), key = RabbitMQConfig.RK_CV_PARSED_FAILED))
    @Transactional
    public void onCvParsedFailed(CVParseFailedEvent event) {
        log.info("Received cv.parsed.failed (compensation): applicationId={}, jobId={}, reason={}",
                event.applicationId(), event.jobId(), event.reason());

        JobAggregate job = jobRepository.findById(event.jobId())
                .orElseThrow(() -> new JobNotFoundException(event.jobId()));

        job.decrementApplicantCount();
        jobRepository.save(job);

        log.info("Compensation completed: decremented applicant count for jobId={}, new count={}",
                event.jobId(), job.getApplicantCount());
    }
}
