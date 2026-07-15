package com.talenthub.application.infrastructure.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenthub.application.domain.model.OutboxEvent;
import com.talenthub.application.domain.repository.OutboxEventRepository;
import com.talenthub.application.infrastructure.messaging.ApplicationEventPublisher;
import com.talenthub.events.ApplicationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageRelayListener {

    private final OutboxEventRepository outboxEventRepository;
    //    private final RabbitTemplate rabbitTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 10000)
    public void pollAndPublish() {

        List<OutboxEvent> outboxEvents = outboxEventRepository.findUnPublished(false);

        outboxEvents.forEach(outboxEvent -> {
            try {
                String routingKey = outboxEvent.getEventType();
                switch (routingKey) {
                    case "application.created": {
                        eventPublisher.publishApplicationCreated(objectMapper.readValue(outboxEvent.getPayload(), ApplicationCreatedEvent.class));

                        // Update processed = true
                        outboxEvent.markProcessed();
                        outboxEventRepository.save(outboxEvent);
                    }
                }
            } catch (Exception ex) {
                log.error("Exception in publish application created event", ex);
            }

        });


    }
}
