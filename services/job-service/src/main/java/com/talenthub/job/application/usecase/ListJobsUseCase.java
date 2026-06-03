package com.talenthub.job.application.usecase;

import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListJobsUseCase {
    private final JobRepository jobRepository;

    @Transactional(readOnly = true)
    public Page<Job> all(String keyword, Pageable pageable) {
        return jobRepository.search(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Job> published(String keyword, Pageable pageable) {
        return jobRepository.searchPublished(keyword, pageable);
    }
}
