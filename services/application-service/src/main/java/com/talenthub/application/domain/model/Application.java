package com.talenthub.application.domain.model;

import com.talenthub.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate Root: vòng đời 1 đơn ứng tuyển + state machine pipeline (BRULE-12).
 */
@Getter
@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_candidate_job",
                columnNames = {"candidate_id", "job_id"}))
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_stage", nullable = false, length = 30)
    private PipelineStage currentStage;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    /**
     * Note đánh giá của Recruiter/HM qua từng stage.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private List<EvaluationNote> notes = new ArrayList<>();

    /**
     * Sub-aggregate: lịch & kết quả phỏng vấn (tối đa 1).
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    /**
     * Sub-aggregate: offer cuối cùng (tối đa 1).
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    // ---------------------------------------------------------------------
    //  Factory
    // ---------------------------------------------------------------------

    /**
     * Tạo đơn mới — luôn bắt đầu ở stage NEW.
     */
    public static Application submit(UUID candidateId, UUID jobId) {
        if (candidateId == null || jobId == null) {
            throw new IllegalArgumentException("candidateId and jobId are required");
        }
        Application app = new Application();
        app.candidateId = candidateId;
        app.jobId = jobId;
        app.currentStage = PipelineStage.NEW;
        app.submittedAt = Instant.now();
        return app;
    }

    // ---------------------------------------------------------------------
    //  State machine (BRULE-12: không nhảy cóc, không lùi)
    // ---------------------------------------------------------------------

    public void advanceStage(PipelineStage next) {
        if (!canAdvanceTo(next)) {
            throw new IllegalStateException(
                    "Invalid transition from " + currentStage + " to " + next);
        }
        this.currentStage = next;
    }

    private boolean canAdvanceTo(PipelineStage next) {
        return switch (currentStage) {
            case NEW -> next == PipelineStage.CV_SCREENING || next == PipelineStage.REJECTED;
            case CV_SCREENING -> next == PipelineStage.INTERVIEW_TECHNICAL
                    || next == PipelineStage.REJECTED
                    || next == PipelineStage.TRANSFERRED;
            case INTERVIEW_TECHNICAL -> next == PipelineStage.INTERVIEW_HM
                    || next == PipelineStage.REJECTED;
            case INTERVIEW_HM -> next == PipelineStage.OFFERED
                    || next == PipelineStage.REJECTED;
            case OFFERED -> next == PipelineStage.HIRED
                    || next == PipelineStage.OFFER_DECLINED;
            default -> false; // HIRED, REJECTED, OFFER_DECLINED, TRANSFERRED là terminal
        };
    }

    /**
     * Đặt lịch phỏng vấn — chỉ hợp lệ khi đang ở giai đoạn sàng lọc / phỏng vấn kỹ thuật (chuyên môn).
     */
    public void scheduleInterview(LocalDateTime when, String interviewerName) {
        if (currentStage != PipelineStage.CV_SCREENING
                && currentStage != PipelineStage.INTERVIEW_TECHNICAL) {
            throw new IllegalStateException("Sai stage để schedule interview: " + currentStage);
        }
        this.interview = Interview.schedule(when, interviewerName);
    }

    /**
     * Ghi nhận kết quả phỏng vấn.
     */
    public void completeInterview(InterviewFeedback feedback) {
        if (interview == null) {
            throw new IllegalStateException("Chưa có interview để complete");
        }
        interview.complete(feedback);
    }

    /**
     * Gửi offer — phải qua vòng HM rồi mới được offer; tự động chuyển sang OFFERED.
     */
    public void extendOffer(BigDecimal salary, LocalDate startDate) {
        if (currentStage != PipelineStage.INTERVIEW_HM) {
            throw new IllegalStateException("Phải qua INTERVIEW_HM mới offer được");
        }
        this.offer = Offer.extend(salary, startDate);
        advanceStage(PipelineStage.OFFERED);
    }

    /**
     * Ứng viên chấp nhận offer → HIRED.
     */
    public void acceptOffer() {
        if (offer == null) {
            throw new IllegalStateException("Chưa có offer để accept");
        }
        offer.accept();
        advanceStage(PipelineStage.HIRED);
    }

    /**
     * Ứng viên từ chối offer → OFFER_DECLINED.
     */
    public void declineOffer() {
        if (offer == null) {
            throw new IllegalStateException("Chưa có offer để decline");
        }
        offer.decline();
        advanceStage(PipelineStage.OFFER_DECLINED);
    }

    /**
     * Thêm note đánh giá tại stage hiện tại.
     */
    public void addNote(String author, String content) {
        notes.add(EvaluationNote.create(author, content, currentStage));
    }
}
