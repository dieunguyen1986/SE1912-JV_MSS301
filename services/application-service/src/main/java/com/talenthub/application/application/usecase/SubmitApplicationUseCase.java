package com.talenthub.application.application.usecase;

import com.talenthub.application.api.dto.JobResponse;
import com.talenthub.application.application.command.SubmitApplicationCommand;
import com.talenthub.application.domain.exception.DuplicateApplicationException;
import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.repository.ApplicationRepository;
import com.talenthub.application.infrastructure.feign.JobServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmitApplicationUseCase {

    private final ApplicationRepository repo;
    private final JobServiceClient jobServiceClient;

    @Transactional
    public UUID execute(SubmitApplicationCommand cmd) {
        // BRULE-09: chặn nộp trùng ở tầng application
        if (repo.existsByCandidateIdAndJobId(cmd.candidateId(), cmd.jobId()))
            throw new DuplicateApplicationException(cmd.candidateId(), cmd.jobId());

        JobResponse jobResponse = jobServiceClient.findById(cmd.jobId());

        Application app = Application.submit(cmd.candidateId(), cmd.jobId());
        return repo.save(app).getId();
    }
}