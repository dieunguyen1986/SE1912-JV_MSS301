package com.talenthub.job.application.usecase;

import com.talenthub.job.application.command.DraftJobCommand;
import com.talenthub.job.domain.model.Job;
import com.talenthub.job.domain.repository.JobRepository;
import com.talenthub.job.domain.exception.DuplicateJobException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DraftJobUseCase {
    private final JobRepository jobRepository;

    @Transactional
    public Job execute(DraftJobCommand cmd) {
        if (jobRepository.existsByTitle(cmd.title())) {
            throw new DuplicateJobException(cmd.title());
        }

        Job job = Job.createNewJob(
                cmd.title(), cmd.description(), cmd.location(), cmd.departmentId(),
                cmd.minSalary(), cmd.maxSalary(), cmd.deadline(), cmd.requiredSkills());

        return jobRepository.save(job);
    }
}
