package com.talenthub.application.infrastructure.messaging;

import com.talenthub.events.ApplicationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishApplicationCreated(ApplicationCreatedEvent event) {
        log.info("Publishing ApplicationCreatedEvent: applicationId={}, candidateEmail={}",
                event.applicationId(), event.candidateEmail());

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_APP_CREATED, event);

        log.info("Event published successfully for applicationId={}", event.applicationId());
    }
}
