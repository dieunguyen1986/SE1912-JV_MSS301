package com.talenthub.job.application.usecase;

import com.talenthub.job.application.command.JobCommand;
import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.exception.DuplicateJobTitleException;
import com.talenthub.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateNewJobUseCase {

    private final JobRepository jobRepository;

    @Transactional
    public UUID execute(JobCommand command) {
        if (jobRepository.isExisted(command.getTitle())) {
            throw new DuplicateJobTitleException(command.getTitle());
        }

        JobAggregate aggregate = JobAggregate.createJob(
                command.getTitle(),
                command.getDescription(),
                command.getDepartmentId(),
                command.getMinSalary(),
                command.getMaxSalary(),
                command.getDeadline(),
                command.getMaxApplicants()
        );

        return jobRepository.save(aggregate).getId();
    }
}
