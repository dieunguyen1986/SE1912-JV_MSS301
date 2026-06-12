package com.talenthub.application.api.dto;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.EvaluationNote;
import com.talenthub.application.domain.model.Interview;
import com.talenthub.application.domain.model.InterviewFeedback;
import com.talenthub.application.domain.model.Offer;
import com.talenthub.application.domain.model.PipelineStage;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** View đầy đủ 1 đơn ứng tuyển (pipeline + interview + offer + notes). */
public record ApplicationResponse(
        UUID id,
        UUID candidateId,
        UUID jobId,
        PipelineStage currentStage,
        Instant submittedAt,
        InterviewDto interview,
        OfferDto offer,
        List<NoteDto> notes,
        Instant createdAt,
        Instant updatedAt
) {
    public record InterviewDto(UUID id, LocalDateTime scheduledAt, String interviewerName,
                               String status, FeedbackDto feedback) {}

    public record FeedbackDto(int score, String comment, String interviewerName) {}

    public record OfferDto(UUID id, BigDecimal salary, LocalDate startDate, String status, Instant extendedAt) {}

    public record NoteDto(UUID id, String author, String content, PipelineStage stage, Instant createdAt) {}

    public static ApplicationResponse from(Application app) {
        return new ApplicationResponse(
                app.getId(),
                app.getCandidateId(),
                app.getJobId(),
                app.getCurrentStage(),
                app.getSubmittedAt(),
                toInterview(app.getInterview()),
                toOffer(app.getOffer()),
                app.getNotes().stream().map(ApplicationResponse::toNote).toList(),
                app.getCreatedAt(),
                app.getUpdatedAt());
    }

    private static InterviewDto toInterview(Interview i) {
        if (i == null) return null;
        return new InterviewDto(i.getId(), i.getScheduledAt(), i.getInterviewerName(),
                i.getStatus().name(), toFeedback(i.getFeedback()));
    }

    private static FeedbackDto toFeedback(InterviewFeedback f) {
        if (f == null) return null;
        return new FeedbackDto(f.getScore(), f.getComment(), f.getInterviewerName());
    }

    private static OfferDto toOffer(Offer o) {
        if (o == null) return null;
        return new OfferDto(o.getId(), o.getSalary(), o.getStartDate(), o.getStatus().name(), o.getExtendedAt());
    }

    private static NoteDto toNote(EvaluationNote n) {
        return new NoteDto(n.getId(), n.getAuthor(), n.getContent(), n.getStage(), n.getCreatedAt());
    }
}
