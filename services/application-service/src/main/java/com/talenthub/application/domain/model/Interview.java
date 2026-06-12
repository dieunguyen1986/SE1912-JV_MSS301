package com.talenthub.application.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity con của Application: 1 buổi phỏng vấn. Truy cập qua aggregate root
 * ({@link Application#scheduleInterview}, {@link Application#completeInterview}).
 */
@Getter
@Entity
@Table(name = "interviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interview {

    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "interviewer_name", nullable = false)
    private String interviewerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Embedded
    private InterviewFeedback feedback;

    static Interview schedule(LocalDateTime when, String interviewerName) {
        if (when == null || when.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian phỏng vấn phải ở tương lai");
        }
        if (interviewerName == null || interviewerName.isBlank()) {
            throw new IllegalArgumentException("interviewerName must not be blank");
        }
        Interview i = new Interview();
        i.id = UUID.randomUUID();
        i.scheduledAt = when;
        i.interviewerName = interviewerName.trim();
        i.status = Status.SCHEDULED;
        return i;
    }

    void complete(InterviewFeedback feedback) {
        if (status != Status.SCHEDULED) {
            throw new IllegalStateException("Chỉ interview SCHEDULED mới complete được");
        }
        if (feedback == null) {
            throw new IllegalArgumentException("feedback must not be null");
        }
        this.feedback = feedback;
        this.status = Status.COMPLETED;
    }

    void cancel() {
        if (status != Status.SCHEDULED) {
            throw new IllegalStateException("Chỉ interview SCHEDULED mới cancel được");
        }
        this.status = Status.CANCELLED;
    }
}
