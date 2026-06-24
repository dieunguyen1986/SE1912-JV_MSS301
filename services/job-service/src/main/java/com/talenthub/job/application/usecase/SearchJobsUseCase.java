package com.talenthub.job.application.usecase;

import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.repository.JobRepository;
import com.talenthub.job.infrastructure.specification.JobSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchJobsUseCase {

    private final JobRepository jobRepository;

    @Transactional(readOnly = true)
    public Page<JobAggregate> execute(JobSearchCriteria criteria, Pageable pageable) {
        return jobRepository.search(criteria, pageable);
    }
}
