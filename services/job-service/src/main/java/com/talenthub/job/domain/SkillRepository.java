package com.talenthub.job.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SkillRepository {
    Skill save(Skill skill);

    Optional<Skill> findById(UUID id);

    List<Skill> findAll();

    boolean existsByName(String name);
}
