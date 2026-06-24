package com.talenthub.job.infrastructure.adapter;

import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.repository.JobRepository;
import com.talenthub.job.infrastructure.persistence.JobJpaRepository;
import com.talenthub.job.infrastructure.persistence.entity.Job;
import com.talenthub.job.infrastructure.persistence.mapper.JobPersistenceMapper;
import com.talenthub.job.infrastructure.specification.JobSearchCriteria;
import com.talenthub.job.infrastructure.specification.JobSpecifications;
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
    public JobAggregate save(JobAggregate aggregate) {
        Job entity;
        if (aggregate.getId() == null) {
            entity = JobPersistenceMapper.toNewEntity(aggregate);
        } else {
            entity = jobJpaRepository.findById(aggregate.getId())
                    .orElseGet(() -> JobPersistenceMapper.toNewEntity(aggregate));
            JobPersistenceMapper.applyToEntity(aggregate, entity);
        }
        return JobPersistenceMapper.toAggregate(jobJpaRepository.save(entity));
    }

    @Override
    public Optional<JobAggregate> findById(UUID id) {
        return jobJpaRepository.findById(id).map(JobPersistenceMapper::toAggregate);
    }

    @Override
    public Page<JobAggregate> search(JobSearchCriteria criteria, Pageable pageable) {
        return jobJpaRepository.findAll(JobSpecifications.fromCriteria(criteria), pageable)
                .map(JobPersistenceMapper::toAggregate);
    }

    @Override
    public boolean isExisted(String jobTitle) {
        return jobJpaRepository.existsByTitle(jobTitle);
    }

    @Override
    public void delete(UUID id) {
        jobJpaRepository.findById(id).ifPresent(entity -> {
            entity.softDelete();
            jobJpaRepository.save(entity);
        });
    }
}
