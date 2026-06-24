package com.talenthub.job.application.usecase;

import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetJobByIdUseCase {

    private final JobRepository jobRepository;

    @Transactional(readOnly = true)
    public JobAggregate execute(UUID id) {
        return jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException(id));
    }
}
