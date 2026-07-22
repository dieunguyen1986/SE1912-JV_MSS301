package com.talenthub.application.infrastructure.messaging;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;
import com.talenthub.application.domain.repository.ApplicationRepository;
import com.talenthub.events.CVParseFailedEvent;
import com.talenthub.events.CVParsedEvent;
import com.talenthub.events.JobSlotRejectedEvent;
import com.talenthub.events.JobSlotReservedEvent;
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
public class ApplicationResultListener {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationRepository applicationRepository;

    // Compensating Transaction
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_JOB_SLOT_REJECTED, durable = "true"),
            exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_JOB_SLOT_REJECTED
    ))
    @Transactional
    public void updateRejectedApplication(JobSlotRejectedEvent event) {
        Optional<Application> optional = applicationRepository.findById(event.applicationId());

        Application application = optional.orElseThrow(() -> {
            throw new RuntimeException("Application not found id = " + event.applicationId());
        });

        application.advanceStage(PipelineStage.REJECTED);
        application.addNote("SYSTEM", "Hết slot: " + event.reason());

        applicationRepository.save(application);

        log.info("Application {} updated to REJECTED due to job slot full. Reason: {}", application.getId(), event.reason());
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_APPLICATION_CV_PARSED_SUCCESS, durable = "true"),
            exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_CV_PARSED_SUCCESS
    ))
    @Transactional
    public void onCvParsedSuccess(CVParsedEvent event) {
        log.info("Received CVParsedEvent for applicationId: {}", event.applicationId());

        // Advance to CV_SCREENING
        Application app = applicationRepository.findById(event.applicationId()).orElseThrow(() -> new RuntimeException("App not found"));
        if (app.getCurrentStage() == PipelineStage.NEW) {
            app.advanceStage(PipelineStage.CV_SCREENING);
            applicationRepository.save(app);
            log.info("Application {} advanced to CV_SCREENING.", app.getId());
        }
    }

    /**
     * Compensation: CV parse thất bại thì application chuyển REJECTED.
     * job-service cũng lắng nghe event này để decrement applicant count.
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.QUEUE_APPLICATION_CV_PARSED_FAILED, durable = "true"),
            exchange = @Exchange(name = RabbitMQConfig.EXCHANGE_NAME, type = "topic"),
            key = RabbitMQConfig.RK_CV_PARSED_FAILED
    ))
    @Transactional
    public void onCvParsedFailed(CVParseFailedEvent event) {
        log.info("Received CVParseFailedEvent for applicationId: {}, reason: {}",
                event.applicationId(), event.reason());

        Application app = applicationRepository.findById(event.applicationId())
                .orElseThrow(() -> new RuntimeException("Application not found: " + event.applicationId()));

        app.advanceStage(PipelineStage.REJECTED);
        app.addNote("SYSTEM", "CV không phù hợp: " + event.reason());
        applicationRepository.save(app);

        log.info("Application {} updated to REJECTED due to CV parse failure.", app.getId());
    }
}
