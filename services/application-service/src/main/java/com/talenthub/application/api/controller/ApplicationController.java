package com.talenthub.application.api.controller;

import com.talenthub.application.api.dto.AdvanceStageRequest;
import com.talenthub.application.api.dto.ApplicationResponse;
import com.talenthub.application.api.dto.SubmitApplicationRequest;
import com.talenthub.application.application.command.SubmitApplicationCommand;
import com.talenthub.application.application.usecase.AdvanceStageUseCase;
import com.talenthub.application.application.usecase.GetApplicationUseCase;
import com.talenthub.application.application.usecase.SubmitApplicationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final SubmitApplicationUseCase submitUseCase;
    private final AdvanceStageUseCase advanceUseCase;
    private final GetApplicationUseCase getUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public ResponseEntity<Map<String, UUID>> submit(@Valid @RequestBody SubmitApplicationRequest req) {
        UUID applicationId = submitUseCase.execute(new SubmitApplicationCommand(req.candidateId(), req.jobId(), req.cvFileUrl()));

        return ResponseEntity.created(URI.create("/api/v1/applications/" +
                applicationId)).body(Map.of("id", applicationId, "candidateId", req.candidateId()));
    }

    @PatchMapping("/{id}/stage")
    public ResponseEntity<Void> advance(@PathVariable UUID id, @Valid @RequestBody AdvanceStageRequest req) {

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ApplicationResponse get(@PathVariable UUID id) {
        return ApplicationResponse.from(getUseCase.execute(id));
    }
}