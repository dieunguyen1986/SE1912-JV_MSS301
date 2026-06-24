package com.talenthub.job.domain.repository;

import com.talenthub.job.domain.aggregate.SkillAggregate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillRepository {

    SkillAggregate save(SkillAggregate aggregate);

    Optional<SkillAggregate> findById(UUID id);

    List<SkillAggregate> findAll();

    boolean existsByName(String name);

    void delete(UUID id);
}
