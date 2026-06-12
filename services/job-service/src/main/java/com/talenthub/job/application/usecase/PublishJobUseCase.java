package com.talenthub.job.application.usecase;

import com.talenthub.job.domain.model.Job;
import com.talenthub.job.domain.repository.JobRepository;
import com.talenthub.job.domain.exception.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublishJobUseCase {
    private final JobRepository jobRepository;

    @Transactional
    public Job execute(UUID id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException(id));
        job.publish();
        return jobRepository.save(job);
    }
}
