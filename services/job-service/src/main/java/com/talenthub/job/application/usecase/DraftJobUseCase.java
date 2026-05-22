package com.talenthub.job.application.usecase;

import com.talenthub.job.application.command.DraftJobCommand;
import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.JobRepository;
import com.talenthub.job.domain.exception.DuplicateJobException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DraftJobUseCase {
    private final JobRepository jobRepository;

    public Job execute(DraftJobCommand draftJobCommand) {
        if (jobRepository.existsByTitle(draftJobCommand.getTitle())) {
            throw new DuplicateJobException(draftJobCommand.getTitle());
        }

        // Using aggregate root
        Job job = Job.createNewJob(draftJobCommand.getTitle(), draftJobCommand.getDescription(),
                draftJobCommand.getDepartmentId(), draftJobCommand.getMinSalary(),
                draftJobCommand.getMaxSalary(), draftJobCommand.getDeadline(), draftJobCommand.getRequiredSkills());

        // Entity here
        return jobRepository.createJob(job);
    }
}
