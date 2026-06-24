package com.talenthub.job.infrastructure.persistence.entity;

import com.talenthub.job.domain.vo.SalaryRange;
import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_jobs_status", columnList = "status"),
        @Index(name = "idx_jobs_department", columnList = "department_id")
})
public class Job extends BaseEntity {

    public enum Status {
        DRAFT, PENDING_APPROVAL, APPROVED, PUBLISHED, CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "department_id", nullable = false)
    private UUID departmentId;

    @Embedded
    private SalaryRange salaryRange;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
}
