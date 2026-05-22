package com.talenthub.job.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "job_skills",
       uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "skill_id"}))
@EqualsAndHashCode(of = {"jobId", "skillId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobSkill {

    public enum Level {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(name = "skill_id", nullable = false)
    private UUID skillId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Level requiredLevel;

    @Column(name = "is_mandatory", nullable = false)
    private boolean mandatory;

    static JobSkill of(UUID jobId, UUID skillId, Level requiredLevel, boolean mandatory) {
        if (jobId == null || skillId == null) {
            throw new IllegalArgumentException("jobId/skillId must not be null");
        }
        if (requiredLevel == null) {
            throw new IllegalArgumentException("requiredLevel must not be null");
        }
        JobSkill js = new JobSkill();
        js.id = UUID.randomUUID();
        js.jobId = jobId;
        js.skillId = skillId;
        js.requiredLevel = requiredLevel;
        js.mandatory = mandatory;
        return js;
    }

    void upgradeLevel(Level newLevel) {
        if (newLevel == null) {
            throw new IllegalArgumentException("newLevel must not be null");
        }
        this.requiredLevel = newLevel;
    }
}
