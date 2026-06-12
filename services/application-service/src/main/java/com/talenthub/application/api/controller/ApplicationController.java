package com.talenthub.application.api.controller;

import com.talenthub.application.api.dto.AddNoteRequest;
import com.talenthub.application.api.dto.AdvanceStageRequest;
import com.talenthub.application.api.dto.ApplicationResponse;
import com.talenthub.application.api.dto.ApplicationSummaryResponse;
import com.talenthub.application.api.dto.CompleteInterviewRequest;
import com.talenthub.application.api.dto.ExtendOfferRequest;
import com.talenthub.application.api.dto.OfferDecisionRequest;
import com.talenthub.application.api.dto.PageResponse;
import com.talenthub.application.api.dto.ScheduleInterviewRequest;
import com.talenthub.application.api.dto.SubmitApplicationRequest;
import com.talenthub.application.application.command.AddNoteCommand;
import com.talenthub.application.application.command.CompleteInterviewCommand;
import com.talenthub.application.application.command.ExtendOfferCommand;
import com.talenthub.application.application.command.ScheduleInterviewCommand;
import com.talenthub.application.application.command.SubmitApplicationCommand;
import com.talenthub.application.application.usecase.AddEvaluationNoteUseCase;
import com.talenthub.application.application.usecase.AdvanceStageUseCase;
import com.talenthub.application.application.usecase.CompleteInterviewUseCase;
import com.talenthub.application.application.usecase.DecideOfferUseCase;
import com.talenthub.application.application.usecase.ExtendOfferUseCase;
import com.talenthub.application.application.usecase.GetApplicationUseCase;
import com.talenthub.application.application.usecase.ListApplicationsUseCase;
import com.talenthub.application.application.usecase.ScheduleInterviewUseCase;
import com.talenthub.application.application.usecase.SubmitApplicationUseCase;
import com.talenthub.application.domain.model.PipelineStage;
import com.talenthub.application.utils.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.APPLICATIONS)
@RequiredArgsConstructor
public class ApplicationController {

    private final SubmitApplicationUseCase submitUseCase;
    private final AdvanceStageUseCase advanceUseCase;
    private final GetApplicationUseCase getUseCase;
    private final ListApplicationsUseCase listUseCase;
    private final ScheduleInterviewUseCase scheduleInterviewUseCase;
    private final CompleteInterviewUseCase completeInterviewUseCase;
    private final ExtendOfferUseCase extendOfferUseCase;
    private final DecideOfferUseCase decideOfferUseCase;
    private final AddEvaluationNoteUseCase addNoteUseCase;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> submit(@Valid @RequestBody SubmitApplicationRequest req) {
        UUID id = submitUseCase.execute(new SubmitApplicationCommand(req.candidateId(), req.jobId()));
        return ResponseEntity.created(URI.create(ApiPaths.APPLICATIONS + "/" + id))
                .body(Map.of("id", id));
    }

    @GetMapping(ApiPaths.BY_ID)
    public ApplicationResponse getById(@PathVariable UUID id) {
        return ApplicationResponse.from(getUseCase.execute(id));
    }

    /** UC-02.7b — Kanban board: lọc theo job và/hoặc stage. */
    @GetMapping
    public PageResponse<ApplicationSummaryResponse> list(
            @RequestParam(required = false) UUID jobId,
            @RequestParam(required = false) PipelineStage stage,
            @PageableDefault(size = 20) Pageable pageable) {
        return PageResponse.of(
                listUseCase.execute(jobId, stage, pageable),
                ApplicationSummaryResponse::from);
    }

    /** UC-02.7 — Move Candidate Stage (Kanban drag-and-drop), state machine BRULE-12. */
    @PatchMapping(ApiPaths.STAGE)
    public ResponseEntity<Void> advance(@PathVariable UUID id, @Valid @RequestBody AdvanceStageRequest req) {
        advanceUseCase.execute(id, req.targetStage());
        return ResponseEntity.noContent().build();
    }

    /** UC-02.8 — Add Evaluation Notes. */
    @PostMapping(ApiPaths.NOTES)
    public ApplicationResponse addNote(@PathVariable UUID id, @Valid @RequestBody AddNoteRequest req) {
        return ApplicationResponse.from(
                addNoteUseCase.execute(new AddNoteCommand(id, req.author(), req.content())));
    }

    /** UC-02.11 / UC-02.15 — Schedule interview. */
    @PostMapping(ApiPaths.INTERVIEW)
    public ApplicationResponse scheduleInterview(@PathVariable UUID id,
                                                 @Valid @RequestBody ScheduleInterviewRequest req) {
        return ApplicationResponse.from(scheduleInterviewUseCase.execute(
                new ScheduleInterviewCommand(id, req.scheduledAt(), req.interviewerName())));
    }

    /** UC-02.12 — Record interview result. */
    @PostMapping(ApiPaths.INTERVIEW_RESULT)
    public ApplicationResponse completeInterview(@PathVariable UUID id,
                                                 @Valid @RequestBody CompleteInterviewRequest req) {
        return ApplicationResponse.from(completeInterviewUseCase.execute(
                new CompleteInterviewCommand(id, req.score(), req.comment(), req.interviewerName())));
    }

    /** Gửi offer (ứng viên đã qua INTERVIEW_HM) → OFFERED. */
    @PostMapping(ApiPaths.OFFER)
    public ApplicationResponse extendOffer(@PathVariable UUID id,
                                           @Valid @RequestBody ExtendOfferRequest req) {
        return ApplicationResponse.from(extendOfferUseCase.execute(
                new ExtendOfferCommand(id, req.salary(), req.startDate())));
    }

    /** UC-02.18 — Update Offer Status: ACCEPT → HIRED / DECLINE → OFFER_DECLINED. */
    @PostMapping(ApiPaths.OFFER_DECISION)
    public ApplicationResponse decideOffer(@PathVariable UUID id,
                                           @Valid @RequestBody OfferDecisionRequest req) {
        return ApplicationResponse.from(decideOfferUseCase.execute(id, req.decision()));
    }
}
