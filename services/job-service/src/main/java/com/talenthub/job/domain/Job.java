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

    private String title;
    private String description;

    @Column(columnDefinition = "VARCHAR(500)")
    private String location;

    @Column(name = "min_salary", columnDefinition = "NUMERIC(15,2)")
    private BigDecimal minSalary;

    @Column(name = "max_salary", columnDefinition = "NUMERIC(15,2)")
    private BigDecimal maxSalary;
    private LocalDate deadline;
    private Status status;
    private Set<String> requiredSkills = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    public static Job createNewJob(String title, String description, UUID departmentId, BigDecimal minSalary, BigDecimal maxSalary, LocalDate deadline, Set<String> requiredSkills) {
        if (minSalary == null || maxSalary == null || minSalary.signum() < 0 || maxSalary.signum() < 0) {
            throw new IllegalArgumentException("Salary needs to be greater than 0");

        }

        if (minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("minSalary must be <=maxSalary");
        }

        if (requiredSkills == null || requiredSkills.isEmpty()) {
            throw new IllegalArgumentException("Required skills needs to be set");
        }


        Job job = new Job();
        job.title = title;
        job.description = description;

        if (departmentId != null) {
            Department department = Department.getInstance(departmentId);
            job.department = department;
        }

        job.minSalary = minSalary;
        job.maxSalary = maxSalary;
        job.deadline = deadline;
        job.status = Status.DRAFT;


        return job;
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
        this.status = Status.CLOSED;
    }
}
