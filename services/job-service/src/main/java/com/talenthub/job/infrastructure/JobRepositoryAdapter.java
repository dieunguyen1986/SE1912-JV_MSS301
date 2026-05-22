package com.talenthub.job.infrastructure;

import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepository {
    private final JobJpaRepository jobJpaRepository;

    @Override
    public Job createJob(Job job) {
        return jobJpaRepository.save(job);
    }

    @Override
    public Page<Job> getJobs(Pageable pageable, String keyword) {
        return jobJpaRepository.getJobs(pageable, keyword);
    }

    @Override
    public UUID requestApprove(Job job) {
        return jobJpaRepository.save(job).getId();
    }

    @Override
    public boolean existsByTitle(String title) {
        return jobJpaRepository.existsByTitle(title);
    }
}
