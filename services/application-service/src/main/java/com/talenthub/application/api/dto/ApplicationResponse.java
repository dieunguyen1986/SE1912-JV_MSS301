package com.talenthub.application.api.dto;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.EvaluationNote;
import com.talenthub.application.domain.model.Interview;
import com.talenthub.application.domain.model.Offer;
import com.talenthub.application.domain.model.PipelineStage;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * View đầy đủ của 1 Application trả ra ngoài (đọc qua aggregate root).
 * Không expose entity JPA trực tiếp — map sang DTO bất biến.
 */
public record ApplicationResponse(
        UUID id,
        UUID candidateId,
        UUID jobId,
        PipelineStage currentStage,
        Instant submittedAt,
        List<NoteDto> notes,
        InterviewDto interview,
        OfferDto offer,
        Instant createdAt,
        Instant updatedAt
) {

    public record NoteDto(String author, String content, PipelineStage stage, Instant createdAt) {}

    public record InterviewDto(UUID id, LocalDateTime scheduledAt, String interviewerName,
                               String status, FeedbackDto feedback) {}

    public record FeedbackDto(int score, String comment, String interviewerName) {}

    public record OfferDto(UUID id, BigDecimal salary, LocalDate startDate, String status, Instant extendedAt) {}

    public static ApplicationResponse from(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getCandidateId(),
                a.getJobId(),
                a.getCurrentStage(),
                a.getSubmittedAt(),
                a.getNotes().stream().map(ApplicationResponse::toNoteDto).toList(),
                toInterviewDto(a.getInterview()),
                toOfferDto(a.getOffer()),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }

    private static NoteDto toNoteDto(EvaluationNote n) {
        return new NoteDto(n.getAuthor(), n.getContent(), n.getStage(), n.getCreatedAt());
    }

    private static InterviewDto toInterviewDto(Interview i) {
        if (i == null) return null;
        FeedbackDto fb = i.getFeedback() == null ? null
                : new FeedbackDto(i.getFeedback().getScore(),
                                  i.getFeedback().getComment(),
                                  i.getFeedback().getInterviewerName());
        return new InterviewDto(i.getId(), i.getScheduledAt(), i.getInterviewerName(),
                i.getStatus().name(), fb);
    }

    private static OfferDto toOfferDto(Offer o) {
        if (o == null) return null;
        return new OfferDto(o.getId(), o.getSalary(), o.getStartDate(), o.getStatus().name(), o.getExtendedAt());
    }
}
