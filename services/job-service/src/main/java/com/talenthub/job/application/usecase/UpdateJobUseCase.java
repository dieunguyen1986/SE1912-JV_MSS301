package com.talenthub.job.application.usecase;

import com.talenthub.job.application.command.JobCommand;
import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateJobUseCase {

    private final JobRepository jobRepository;

    @Transactional
    public JobAggregate execute(UUID id, JobCommand command) {
        JobAggregate aggregate = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
        aggregate.update(
                command.getTitle(),
                command.getDescription(),
                command.getDepartmentId(),
                command.getMinSalary(),
                command.getMaxSalary(),
                command.getDeadline());
        return jobRepository.save(aggregate);
    }
}
