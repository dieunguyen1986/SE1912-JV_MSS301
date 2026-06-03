package com.talenthub.job.api.dto;

import com.talenthub.job.domain.Job;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String title,
        String description,
        String location,
        UUID departmentId,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        LocalDate deadline,
        Job.Status status,
        Set<String> requiredSkills,
        Instant createdAt,
        Instant updatedAt
) {
    public static JobResponse from(Job j) {
        return new JobResponse(
                j.getId(),
                j.getTitle(),
                j.getDescription(),
                j.getLocation(),
                j.getDepartment() == null ? null : j.getDepartment().getId(),
                j.getMinSalary(),
                j.getMaxSalary(),
                j.getDeadline(),
                j.getStatus(),
                j.getRequiredSkills() == null ? Set.of() : Set.copyOf(j.getRequiredSkills()),
                j.getCreatedAt(),
                j.getUpdatedAt()
        );
    }
}
