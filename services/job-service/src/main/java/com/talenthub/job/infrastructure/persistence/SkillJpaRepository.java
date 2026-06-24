package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.infrastructure.persistence.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillJpaRepository extends JpaRepository<Skill, UUID> {
    boolean existsByName(String name);
}
