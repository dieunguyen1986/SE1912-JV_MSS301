package com.talenthub.job.infrastructure.persistence.mapper;

import com.talenthub.job.domain.aggregate.DepartmentAggregate;
import com.talenthub.job.infrastructure.persistence.entity.Department;

public final class DepartmentPersistenceMapper {

    private DepartmentPersistenceMapper() {
    }

    public static DepartmentAggregate toAggregate(Department entity) {
        return DepartmentAggregate.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getManagerId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static Department toNewEntity(DepartmentAggregate aggregate) {
        return Department.builder()
                .name(aggregate.getName())
                .managerId(aggregate.getManagerId())
                .build();
    }

    public static void applyToEntity(DepartmentAggregate aggregate, Department entity) {
        entity.setName(aggregate.getName());
        entity.setManagerId(aggregate.getManagerId());
    }
}
