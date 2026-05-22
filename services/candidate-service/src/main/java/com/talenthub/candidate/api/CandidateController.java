package com.talenthub.candidate.api;

import com.talenthub.candidate.api.dto.RegisterCandidateRequest;
import com.talenthub.candidate.application.command.RegisterCandidateCommand;
import com.talenthub.candidate.application.usecase.RegisterCandidateUseCase;
import com.talenthub.candidate.domain.Candidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.CANDIDATES)
@RequiredArgsConstructor
public class CandidateController {
    private final RegisterCandidateUseCase registerUseCase;

    @PostMapping
    public ResponseEntity<UUID> register(@Valid @RequestBody RegisterCandidateRequest req) {
        UUID id = registerUseCase.execute(new RegisterCandidateCommand(req.fullName(), req.email(), req.phone(), req.address()));

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
