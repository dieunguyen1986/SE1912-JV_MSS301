package com.talenthub.job.infrastructure.adapter;

import com.talenthub.job.domain.aggregate.SkillAggregate;
import com.talenthub.job.domain.repository.SkillRepository;
import com.talenthub.job.infrastructure.persistence.SkillJpaRepository;
import com.talenthub.job.infrastructure.persistence.entity.Skill;
import com.talenthub.job.infrastructure.persistence.mapper.SkillPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SkillRepositoryAdapter implements SkillRepository {

    private final SkillJpaRepository skillJpaRepository;

    @Override
    public SkillAggregate save(SkillAggregate aggregate) {
        Skill entity;
        if (aggregate.getId() == null) {
            entity = SkillPersistenceMapper.toNewEntity(aggregate);
        } else {
            entity = skillJpaRepository.findById(aggregate.getId())
                    .orElseGet(() -> SkillPersistenceMapper.toNewEntity(aggregate));
            SkillPersistenceMapper.applyToEntity(aggregate, entity);
        }
        return SkillPersistenceMapper.toAggregate(skillJpaRepository.save(entity));
    }

    @Override
    public Optional<SkillAggregate> findById(UUID id) {
        return skillJpaRepository.findById(id).map(SkillPersistenceMapper::toAggregate);
    }

    @Override
    public List<SkillAggregate> findAll() {
        return skillJpaRepository.findAll().stream()
                .map(SkillPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return skillJpaRepository.existsByName(name);
    }

    @Override
    public void delete(UUID id) {
        skillJpaRepository.findById(id).ifPresent(entity -> {
            entity.softDelete();
            skillJpaRepository.save(entity);
        });
    }
}
