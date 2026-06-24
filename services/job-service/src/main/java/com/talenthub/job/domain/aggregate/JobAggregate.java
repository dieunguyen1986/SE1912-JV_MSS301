package com.talenthub.job.domain.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobAggregate {

    public enum Status {
        DRAFT, PENDING_APPROVAL, APPROVED, PUBLISHED, CLOSED
    }

    private UUID id;
    private String title;
    private String description;
    private UUID departmentId;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate deadline;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;

    public static JobAggregate createJob(String title, String description, UUID departmentId,
                                         BigDecimal minSalary, BigDecimal maxSalary, LocalDate deadline) {
        validateSalary(minSalary, maxSalary);
        validateDeadline(deadline);

        JobAggregate job = new JobAggregate();
        job.title = title;
        job.description = description;
        job.departmentId = departmentId;
        job.minSalary = minSalary;
        job.maxSalary = maxSalary;
        job.deadline = deadline;
        job.status = Status.DRAFT;
        return job;
    }

    public static JobAggregate reconstitute(UUID id, String title, String description, UUID departmentId,
                                            BigDecimal minSalary, BigDecimal maxSalary, LocalDate deadline,
                                            Status status, Instant createdAt, Instant updatedAt) {
        JobAggregate job = new JobAggregate();
        job.id = id;
        job.title = title;
        job.description = description;
        job.departmentId = departmentId;
        job.minSalary = minSalary;
        job.maxSalary = maxSalary;
        job.deadline = deadline;
        job.status = status;
        job.createdAt = createdAt;
        job.updatedAt = updatedAt;
        return job;
    }

    public void update(String title, String description, UUID departmentId,
                       BigDecimal minSalary, BigDecimal maxSalary, LocalDate deadline) {
        if (status != Status.DRAFT && status != Status.PENDING_APPROVAL) {
            throw new IllegalStateException("Only DRAFT or PENDING_APPROVAL job can be updated");
        }
        validateSalary(minSalary, maxSalary);
        validateDeadline(deadline);

        this.title = title;
        this.description = description;
        this.departmentId = departmentId;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.deadline = deadline;
    }

    public void submitForApproval() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException("Only DRAFT can be submitted");
        }
        this.status = Status.PENDING_APPROVAL;
    }

    public void approve() {
        if (status != Status.PENDING_APPROVAL) {
            throw new IllegalStateException("Only PENDING_APPROVAL can be approved");
        }
        this.status = Status.APPROVED;
    }

    public void publish() {
        if (status != Status.APPROVED) {
            throw new IllegalStateException("Only APPROVED can be published");
        }
        this.status = Status.PUBLISHED;
    }

    public void close() {
        if (status == Status.CLOSED) {
            throw new IllegalStateException("Job is already CLOSED");
        }
        this.status = Status.CLOSED;
    }

    private static void validateSalary(BigDecimal min, BigDecimal max) {
        if (min == null || max == null || min.signum() <= 0 || max.signum() <= 0) {
            throw new IllegalArgumentException("Salary must be greater than 0");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("minSalary must be <= maxSalary");
        }
    }

    private static void validateDeadline(LocalDate deadline) {
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must be today or in the future");
        }
    }
}
