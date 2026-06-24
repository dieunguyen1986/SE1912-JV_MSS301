package com.talenthub.job.infrastructure.persistence.mapper;

import com.talenthub.job.domain.aggregate.SkillAggregate;
import com.talenthub.job.infrastructure.persistence.entity.Skill;

public final class SkillPersistenceMapper {

    private SkillPersistenceMapper() {
    }

    public static SkillAggregate toAggregate(Skill entity) {
        return SkillAggregate.reconstitute(
                entity.getId(),
                entity.getName(),
                SkillAggregate.Type.valueOf(entity.getType().name()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static Skill toNewEntity(SkillAggregate aggregate) {
        return Skill.builder()
                .name(aggregate.getName())
                .type(Skill.Type.valueOf(aggregate.getType().name()))
                .build();
    }

    public static void applyToEntity(SkillAggregate aggregate, Skill entity) {
        entity.setName(aggregate.getName());
        entity.setType(Skill.Type.valueOf(aggregate.getType().name()));
    }
}
