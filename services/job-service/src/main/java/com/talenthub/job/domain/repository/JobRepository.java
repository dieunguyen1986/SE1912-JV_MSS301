package com.talenthub.job.domain.repository;

import com.talenthub.job.domain.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository {
    Job save(Job job);

    Optional<Job> findById(UUID id);

    Page<Job> search(String keyword, Pageable pageable);

    Page<Job> searchPublished(String keyword, Pageable pageable);

    boolean existsByTitle(String title);
}
