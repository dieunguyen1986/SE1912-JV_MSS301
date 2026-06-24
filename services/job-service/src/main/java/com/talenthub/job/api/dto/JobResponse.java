package com.talenthub.job.api.dto;

import com.talenthub.job.domain.aggregate.JobAggregate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private UUID id;
    private String title;
    private String description;
    private UUID departmentId;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate deadline;
    private JobAggregate.Status status;
    private Instant createdAt;
    private Instant updatedAt;

    public static JobResponse from(JobAggregate aggregate) {
        return JobResponse.builder()
                .id(aggregate.getId())
                .title(aggregate.getTitle())
                .description(aggregate.getDescription())
                .departmentId(aggregate.getDepartmentId())
                .minSalary(aggregate.getMinSalary())
                .maxSalary(aggregate.getMaxSalary())
                .deadline(aggregate.getDeadline())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .updatedAt(aggregate.getUpdatedAt())
                .build();
    }
}
