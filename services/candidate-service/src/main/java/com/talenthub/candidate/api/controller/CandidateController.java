package com.talenthub.candidate.api.controller;

import com.talenthub.candidate.api.dto.*;
import com.talenthub.candidate.application.command.AttachCvCommand;
import com.talenthub.candidate.application.command.RegisterCandidateCommand;
import com.talenthub.candidate.application.command.UpdateContactCommand;
import com.talenthub.candidate.application.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.CANDIDATES)
@RequiredArgsConstructor
@Slf4j
public class CandidateController {

    private final RegisterCandidateUseCase registerUseCase;
    private final GetCandidateUseCase getUseCase;
    private final ListCandidatesUseCase listUseCase;
    private final UpdateContactUseCase updateContactUseCase;
    private final AttachCvUseCase attachCvUseCase;
    private final DeleteCandidateUseCase deleteUseCase;
    @Value("${server.port}")
    private String portServer;

    @PostMapping
    public ResponseEntity<CreatedResponse> register(@Valid @RequestBody RegisterCandidateRequest req) {
        UUID id = registerUseCase.execute(
                new RegisterCandidateCommand(req.fullName(), req.email(), req.phone(), req.address()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(id));
    }

    @GetMapping(ApiPaths.CANDIDATE_BY_ID)
    public CandidateResponse getById(@PathVariable UUID id) {
        log.debug("getById called with id={} at port={}", id, portServer);
        return
                CandidateResponse.from(getUseCase.execute(id));
    }

    @GetMapping
    public Page<CandidateSummaryResponse> list(@PageableDefault(size = 20) Pageable pageable) {
        return listUseCase.execute(pageable).map(CandidateSummaryResponse::from);
    }

    @PutMapping(ApiPaths.CONTACT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@PathVariable UUID id,
                              @Valid @RequestBody UpdateContactRequest req) {
        updateContactUseCase.execute(
                new UpdateContactCommand(id, req.email(), req.phone(), req.address()));
    }

    @PostMapping(ApiPaths.CV)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void attachCv(@PathVariable UUID id,
                         @Valid @RequestBody AttachCvRequest req) {
        attachCvUseCase.execute(new AttachCvCommand(id, req.fileUrl(), req.sizeBytes()));
    }

    @DeleteMapping(ApiPaths.CANDIDATE_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
    }
}
