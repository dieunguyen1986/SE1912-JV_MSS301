package com.talenthub.application.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenthub.application.application.command.SubmitApplicationCommand;
import com.talenthub.application.domain.exception.DuplicateApplicationException;
import com.talenthub.application.domain.exception.JobNotOpenForApplicationException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.OutboxEvent;
import com.talenthub.application.domain.repository.ApplicationRepository;
import com.talenthub.application.domain.repository.OutboxEventRepository;
import com.talenthub.application.infrastructure.client.candidate.CandidateServiceClient;
import com.talenthub.application.infrastructure.client.candidate.CandidateView;
import com.talenthub.application.infrastructure.client.job.JobServiceClient;
import com.talenthub.application.infrastructure.client.job.JobView;
import com.talenthub.events.ApplicationCreatedEvent;
import com.talenthub.events.JobSlotReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitApplicationUseCase {

    private final ApplicationRepository repo;
    private final JobServiceClient jobServiceClient;
    private final CandidateServiceClient candidateServiceClient;
    private final OutboxEventRepository outboxEventRepository; // IoC Container - WWebApplicationContext new Adapter
    private final ObjectMapper objectMapper;

    @Transactional
    public UUID execute(SubmitApplicationCommand cmd) {

        JobView job = jobServiceClient.getJobById(cmd.jobId());

        if (!job.isPublished()) {
            throw new JobNotOpenForApplicationException(cmd.jobId(), " not yet published!");
        }

        if (job.isExprired(LocalDate.now())) {
            throw new JobNotOpenForApplicationException(cmd.jobId(), " were expired!");
        }

        // Validate candidate tồn tại (gọi candidate-service qua Eureka LB) - làm TRƯỚC
        // dup check
        // để mọi request đều thực sự gọi sang candidate-service.
        CandidateView candidate = candidateServiceClient.getCandidateById(cmd.candidateId());

        log.info("Candidate={} registering job={}", candidate.fullName(), cmd.jobId());

        // BRULE-09: chặn nộp trùng ở tầng application
        if (repo.existsByCandidateIdAndJobId(cmd.candidateId(), cmd.jobId())) {
            throw new DuplicateApplicationException(cmd.candidateId(), cmd.jobId());
        }

        Application application = Application.submit(cmd.candidateId(), cmd.jobId());

        Application saved = repo.save(application);

//        ApplicationCreatedEvent event = new ApplicationCreatedEvent(
//                UUID.randomUUID(), // eventId: unique cho mỗi event
//                saved.getId(), // applicationId
//                cmd.candidateId(), // candidateId
//                cmd.jobId(), // jobId
//                candidate.email(), // candidateEmail
//                candidate.fullName(), // candidateFullName
//                job.title(), // jobTitle
//                Instant.now() // occurredAt
//        );
//        eventPublisher.publishApplicationCreated(event);


        // Update count of application
        try {
            outboxEventRepository.save(OutboxEvent.create("Application", saved.getId(), "job.application-increment",
                    objectMapper.writeValueAsString(new JobSlotReservedEvent(
                            saved.getId(),
                            cmd.jobId(),
                            cmd.candidateId(),
                            candidate.email(),
                            candidate.fullName(),
                            cmd.cvFileUrl(),
                            job.title()
                    ))
            ));
        } catch (JsonProcessingException e) {
            log.error("Can not publish message to job service: {}", e.getMessage());
        }

        // Parse CV


        // Store to outbox table

//        try {
//            outboxEventRepository.save(OutboxEvent.create("Application", saved.getId(), "application.created",
//                    objectMapper.writeValueAsString(new ApplicationCreatedEvent(
//                            UUID.randomUUID(), // eventId: unique cho mỗi event
//                            saved.getId(), // applicationId
//                            cmd.candidateId(), // candidateId
//                            cmd.jobId(), // jobId
//                            candidate.email(), // candidateEmail
//                            candidate.fullName(), // candidateFullName
//                            job.title(), // jobTitle
//                            Instant.now() // occurredAt
//                    ))));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize for a new application created event");
//        }

        return saved.getId();
    }
}