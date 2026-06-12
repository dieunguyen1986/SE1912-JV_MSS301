package com.talenthub.candidate.api.controller;

import com.talenthub.candidate.utils.ApiPaths;
import com.talenthub.candidate.api.dto.CandidateResponse;
import com.talenthub.candidate.api.dto.CompleteParsingRequest;
import com.talenthub.candidate.api.dto.CvFileResponse;
import com.talenthub.candidate.api.dto.PageResponse;
import com.talenthub.candidate.api.dto.RegisterCandidateRequest;
import com.talenthub.candidate.api.dto.UpdateContactRequest;
import com.talenthub.candidate.application.command.AttachCvCommand;
import com.talenthub.candidate.application.command.RegisterCandidateCommand;
import com.talenthub.candidate.application.command.UpdateContactCommand;
import com.talenthub.candidate.application.usecase.AttachCvUseCase;
import com.talenthub.candidate.application.usecase.CompleteParsingUseCase;
import com.talenthub.candidate.application.usecase.DeleteCandidateUseCase;
import com.talenthub.candidate.application.usecase.FailParsingUseCase;
import com.talenthub.candidate.application.usecase.GetCandidateUseCase;
import com.talenthub.candidate.application.usecase.ListCandidatesUseCase;
import com.talenthub.candidate.application.usecase.RegisterCandidateUseCase;
import com.talenthub.candidate.application.usecase.UpdateContactUseCase;
import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.model.ParsedCvData;
import com.talenthub.candidate.infrastructure.storage.CvStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.CANDIDATES)
@RequiredArgsConstructor
@Validated
public class CandidateController {

    private final RegisterCandidateUseCase registerUseCase;
    private final GetCandidateUseCase getUseCase;
    private final ListCandidatesUseCase listUseCase;
    private final UpdateContactUseCase updateContactUseCase;
    private final AttachCvUseCase attachCvUseCase;
    private final CompleteParsingUseCase completeParsingUseCase;
    private final FailParsingUseCase failParsingUseCase;
    private final DeleteCandidateUseCase deleteUseCase;
    private final CvStorageService cvStorage;

    @PostMapping
    public ResponseEntity<CandidateResponse> register(@Valid @RequestBody RegisterCandidateRequest req) {
        UUID id = registerUseCase.execute(
                new RegisterCandidateCommand(req.fullName(), req.email(), req.phone(), req.address()));
        Candidate created = getUseCase.byId(id);
        return ResponseEntity
                .created(URI.create(ApiPaths.CANDIDATES + "/" + id))
                .body(CandidateResponse.from(created));
    }

    @GetMapping(ApiPaths.BY_ID)
    public CandidateResponse getById(@PathVariable UUID id) {
        return CandidateResponse.from(getUseCase.byId(id));
    }

    @GetMapping(ApiPaths.BY_EMAIL)
    public CandidateResponse getByEmail(@RequestParam @NotBlank @Email String email) {
        return CandidateResponse.from(getUseCase.byEmail(email));
    }

    @GetMapping
    public PageResponse<CandidateResponse> list(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Candidate> page = listUseCase.execute(keyword, pageable);
        return PageResponse.of(page, CandidateResponse::from);
    }

    @PutMapping(ApiPaths.CONTACT)
    public CandidateResponse updateContact(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateContactRequest req) {
        Candidate updated = updateContactUseCase.execute(
                new UpdateContactCommand(id, req.email(), req.phone(), req.address()));
        return CandidateResponse.from(updated);
    }

    @PostMapping(ApiPaths.CV)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public CvFileResponse uploadCv(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file) {
        CvStorageService.StoredFile stored = cvStorage.store(id, file);
        Candidate updated = attachCvUseCase.execute(new AttachCvCommand(id, stored.url(), stored.sizeBytes()));
        return CvFileResponse.from(updated.getCv());
    }

    @PostMapping(ApiPaths.CV_PARSED)
    public CandidateResponse completeParsing(
            @PathVariable UUID id,
            @Valid @RequestBody CompleteParsingRequest req) {
        ParsedCvData data = new ParsedCvData(
                req.skills(), req.educations(), req.experiences(), req.totalYearsExperience());
        return CandidateResponse.from(completeParsingUseCase.execute(id, data));
    }

    @PostMapping(ApiPaths.CV_PARSE_FAILED)
    public CandidateResponse failParsing(@PathVariable UUID id) {
        return CandidateResponse.from(failParsingUseCase.execute(id));
    }

    @DeleteMapping(ApiPaths.BY_ID)
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
    }
}
