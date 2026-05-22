package com.talenthub.job.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobRepository {
    Job createJob(Job job);

    Page<Job> getJobs(Pageable pageable, String keyword);

    UUID requestApprove(Job job);

    boolean existsByTitle(String title);
}
