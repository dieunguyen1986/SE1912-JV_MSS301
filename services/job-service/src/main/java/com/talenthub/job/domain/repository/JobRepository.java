package com.talenthub.job.domain.repository;

import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.infrastructure.specification.JobSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository {

    JobAggregate save(JobAggregate aggregate);

    Optional<JobAggregate> findById(UUID id);

    Page<JobAggregate> search(JobSearchCriteria criteria, Pageable pageable);

    boolean isExisted(String jobTitle);

    void delete(UUID id);
}
