package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.infrastructure.persistence.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JobJpaRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {
    boolean existsByTitle(String jobTitle);
}
