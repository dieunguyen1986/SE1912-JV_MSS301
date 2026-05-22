package com.talenthub.application.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    private UUID id;
    private UUID candidateId;
    private UUID jobId;
    private PipelineStage currentStage;
    private Instant submittedAt;
    private String evaluationNote;


    public static Application submit(UUID candidateId, UUID jobId) {
        if (candidateId == null || jobId == null) {
            throw new IllegalArgumentException("candidateId and jobId are required");
        }
        Application app = new Application();
        app.id = UUID.randomUUID();
        app.candidateId = candidateId;
        app.jobId = jobId;
        app.currentStage = PipelineStage.NEW;
        app.submittedAt = Instant.now();
        return app;
    }

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
            default -> false; // terminal states
        };
    }

    public void addEvaluationNote(String note) { this.evaluationNote = note; }


}