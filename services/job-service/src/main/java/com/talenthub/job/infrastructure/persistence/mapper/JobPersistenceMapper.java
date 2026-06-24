package com.talenthub.job.infrastructure.persistence.mapper;

import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.vo.SalaryRange;
import com.talenthub.job.infrastructure.persistence.entity.Job;

public final class JobPersistenceMapper {

    private JobPersistenceMapper() {
    }

    public static JobAggregate toAggregate(Job entity) {
        SalaryRange sr = entity.getSalaryRange();
        return JobAggregate.reconstitute(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDepartmentId(),
                sr != null ? sr.getMin() : null,
                sr != null ? sr.getMax() : null,
                entity.getDeadline(),
                JobAggregate.Status.valueOf(entity.getStatus().name()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static Job toNewEntity(JobAggregate aggregate) {
        return Job.builder()
                .title(aggregate.getTitle())
                .description(aggregate.getDescription())
                .departmentId(aggregate.getDepartmentId())
                .salaryRange(new SalaryRange(aggregate.getMinSalary(), aggregate.getMaxSalary()))
                .deadline(aggregate.getDeadline())
                .status(Job.Status.valueOf(aggregate.getStatus().name()))
                .build();
    }

    public static void applyToEntity(JobAggregate aggregate, Job entity) {
        entity.setTitle(aggregate.getTitle());
        entity.setDescription(aggregate.getDescription());
        entity.setDepartmentId(aggregate.getDepartmentId());
        entity.setSalaryRange(new SalaryRange(aggregate.getMinSalary(), aggregate.getMaxSalary()));
        entity.setDeadline(aggregate.getDeadline());
        entity.setStatus(Job.Status.valueOf(aggregate.getStatus().name()));
    }
}
