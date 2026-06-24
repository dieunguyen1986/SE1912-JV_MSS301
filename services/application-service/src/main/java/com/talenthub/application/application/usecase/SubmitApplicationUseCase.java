package com.talenthub.application.application.usecase;

import com.talenthub.application.application.command.SubmitApplicationCommand;
import com.talenthub.application.domain.exception.DuplicateApplicationException;
import com.talenthub.application.domain.exception.JobNotOpenForApplicationException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import com.talenthub.application.infrastructure.client.candidate.CandidateServiceClient;
import com.talenthub.application.infrastructure.client.candidate.CandidateView;
import com.talenthub.application.infrastructure.client.job.JobServiceClient;
import com.talenthub.application.infrastructure.client.job.JobView;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitApplicationUseCase {

    private final ApplicationRepository repo;
    private final JobServiceClient jobServiceClient;
    private final CandidateServiceClient candidateServiceClient;

    @Transactional
    public UUID execute(SubmitApplicationCommand cmd) {

        JobView job = jobServiceClient.getJobById(cmd.jobId());


        if (!job.isPublished()) {
            throw new JobNotOpenForApplicationException(cmd.jobId(), " not yet published!");
        }

        if (job.isExprired(LocalDate.now())) {
            throw new JobNotOpenForApplicationException(cmd.jobId(), " were expired!");
        }

        // Validate candidate tồn tại (gọi candidate-service qua Eureka LB) - làm TRƯỚC dup check
        // để mọi request đều thực sự gọi sang candidate-service.
        CandidateView candidate = candidateServiceClient.getCandidateById(cmd.candidateId());

        log.info("Candidate={} registering job={}", candidate.fullName(), cmd.jobId());

        // BRULE-09: chặn nộp trùng ở tầng application
        if (repo.existsByCandidateIdAndJobId(cmd.candidateId(), cmd.jobId())) {
            throw new DuplicateApplicationException(cmd.candidateId(), cmd.jobId());
        }

        Application application = Application.submit(cmd.candidateId(), cmd.jobId());


        return repo.save(application).getId();
    }
}