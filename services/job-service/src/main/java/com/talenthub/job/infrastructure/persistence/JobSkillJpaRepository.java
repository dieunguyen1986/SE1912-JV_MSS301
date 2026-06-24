package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.infrastructure.persistence.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobSkillJpaRepository extends JpaRepository<JobSkill, UUID> {
    List<JobSkill> findByJobId(UUID jobId);

    void deleteByJobId(UUID jobId);
}
