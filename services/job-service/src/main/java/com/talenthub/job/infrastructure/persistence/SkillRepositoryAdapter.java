package com.talenthub.job.infrastructure.persistence;

import com.talenthub.job.domain.model.Skill;
import com.talenthub.job.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SkillRepositoryAdapter implements SkillRepository {
    private final SkillJpaRepository jpa;

    @Override
    public Skill save(Skill skill) {
        return jpa.save(skill);
    }

    @Override
    public Optional<Skill> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public List<Skill> findAll() {
        return jpa.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return jpa.existsByName(name);
    }
}
