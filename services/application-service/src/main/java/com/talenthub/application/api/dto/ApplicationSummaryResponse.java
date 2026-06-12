package com.talenthub.application.api.dto;

import com.talenthub.application.domain.model.Application;
import com.talenthub.application.domain.model.PipelineStage;

import java.time.Instant;
import java.util.UUID;

/** View gọn cho danh sách / Kanban board (không tải sub-aggregate). */
public record ApplicationSummaryResponse(
        UUID id,
        UUID candidateId,
        UUID jobId,
        PipelineStage currentStage,
        Instant submittedAt
) {
    public static ApplicationSummaryResponse from(Application app) {
        return new ApplicationSummaryResponse(
                app.getId(),
                app.getCandidateId(),
                app.getJobId(),
                app.getCurrentStage(),
                app.getSubmittedAt());
    }
}
