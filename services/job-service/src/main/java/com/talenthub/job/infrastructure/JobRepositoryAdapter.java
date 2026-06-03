package com.talenthub.job.infrastructure;

import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepository {
    private final JobJpaRepository jobJpaRepository;

    @Override
    public Job save(Job job) {
        return jobJpaRepository.save(job);
    }

    @Override
    public Optional<Job> findById(UUID id) {
        return jobJpaRepository.findById(id);
    }

    @Override
    public Page<Job> search(String keyword, Pageable pageable) {
        return jobJpaRepository.search(keyword, pageable);
    }

    @Override
    public Page<Job> searchPublished(String keyword, Pageable pageable) {
        return jobJpaRepository.searchPublished(keyword, pageable);
    }

    @Override
    public boolean existsByTitle(String title) {
        return jobJpaRepository.existsByTitle(title);
    }
}
