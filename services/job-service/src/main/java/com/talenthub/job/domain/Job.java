package com.talenthub.job.domain;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    public enum Status {
        DRAFT, PENDING_APPROVAL, APPROVED, PUBLISHED, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String location;

    @Column(name = "min_salary", precision = 15, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 15, scale = 2)
    private BigDecimal maxSalary;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "job_required_skills",
            joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill", length = 100, nullable = false)
    private Set<String> requiredSkills = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    public static Job createNewJob(String title, String description, String location, UUID departmentId,
                                   BigDecimal minSalary, BigDecimal maxSalary, LocalDate deadline,
                                   Set<String> requiredSkills) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (minSalary == null || maxSalary == null || minSalary.signum() < 0 || maxSalary.signum() < 0) {
            throw new IllegalArgumentException("Salary needs to be greater than 0");
        }
        if (minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("minSalary must be <= maxSalary");
        }
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new IllegalArgumentException("Required skills needs to be set");
        }
        if (deadline != null && deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must not be in the past");
        }

        Job job = new Job();
        job.title = title.trim();
        job.description = description;
        job.location = location;
        if (departmentId != null) {
            job.department = Department.getInstance(departmentId);
        }
        job.minSalary = minSalary;
        job.maxSalary = maxSalary;
        job.deadline = deadline;
        job.requiredSkills = new HashSet<>(requiredSkills);
        job.status = Status.DRAFT;
        return job;
    }

    public void updateDetails(String title, String description, String location,
                              BigDecimal minSalary, BigDecimal maxSalary,
                              LocalDate deadline, Set<String> requiredSkills) {
        if (status != Status.DRAFT) {
            throw new IllegalStateException("Only DRAFT can be edited, current: " + status);
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (minSalary == null || maxSalary == null || minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("minSalary must be <= maxSalary");
        }
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new IllegalArgumentException("Required skills needs to be set");
        }
        this.title = title.trim();
        this.description = description;
        this.location = location;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.deadline = deadline;
        this.requiredSkills = new HashSet<>(requiredSkills);
    }

    public void submitForApproval() {
        if (status != Status.DRAFT) {
            throw new IllegalStateException("Only DRAFT can be submitted, current: " + status);
        }
        this.status = Status.PENDING_APPROVAL;
    }

    public void approve() {
        if (status != Status.PENDING_APPROVAL) {
            throw new IllegalStateException("Only PENDING_APPROVAL can be approved, current: " + status);
        }
        this.status = Status.APPROVED;
    }

    public void publish() {
        if (status != Status.APPROVED) {
            throw new IllegalStateException("Only APPROVED can be published, current: " + status);
        }
        this.status = Status.PUBLISHED;
    }

    public void close() {
        if (status == Status.CLOSED) {
            throw new IllegalStateException("Job is already CLOSED");
        }
        this.status = Status.CLOSED;
    }
}
