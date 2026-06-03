package com.talenthub.job.application.usecase;

import com.talenthub.job.application.command.UpdateJobCommand;
import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.JobRepository;
import com.talenthub.job.domain.exception.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateJobUseCase {
    private final JobRepository jobRepository;

    @Transactional
    public Job execute(UpdateJobCommand cmd) {
        Job job = jobRepository.findById(cmd.jobId())
                .orElseThrow(() -> new JobNotFoundException(cmd.jobId()));

        job.updateDetails(cmd.title(), cmd.description(), cmd.location(),
                cmd.minSalary(), cmd.maxSalary(), cmd.deadline(), cmd.requiredSkills());
        return jobRepository.save(job);
    }
}
