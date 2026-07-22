package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.JobCreateRequest;
import com.talenthub.job.api.dto.JobResponse;
import com.talenthub.job.api.dto.JobUpdateRequest;
import com.talenthub.job.api.dto.PageResponse;
import com.talenthub.job.application.command.JobCommand;
import com.talenthub.job.application.usecase.*;
import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.infrastructure.specification.JobSearchCriteria;
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

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    @Value("${server.port}")
    private String serverPort;

    private final CreateNewJobUseCase createNewJobUseCase;
    private final GetJobByIdUseCase getJobByIdUseCase;
    private final SearchJobsUseCase searchJobsUseCase;
    private final UpdateJobUseCase updateJobUseCase;
    private final DeleteJobUseCase deleteJobUseCase;

    @PostMapping
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobCreateRequest request) {
        UUID id = createNewJobUseCase.execute(toCommand(request));
        JobAggregate job = getJobByIdUseCase.execute(id);
        return ResponseEntity.created(URI.create("/api/v1/jobs/" + id)).body(JobResponse.from(job));
    }

    @GetMapping("/{id}")
    public JobResponse getById(@PathVariable("id") UUID id) {

        log.info("Job service instance with port: {}", serverPort);
        return JobResponse.from(getJobByIdUseCase.execute(id));
    }

    @GetMapping
    public PageResponse<JobResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobAggregate.Status status,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) LocalDate deadlineFrom,
            @RequestParam(required = false) LocalDate deadlineTo,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @PageableDefault(size = 20) Pageable pageable) {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .keyword(keyword)
                .status(status)
                .departmentId(departmentId)
                .deadlineFrom(deadlineFrom)
                .deadlineTo(deadlineTo)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .build();
        Page<JobAggregate> page = searchJobsUseCase.execute(criteria, pageable);
        return PageResponse.of(page, JobResponse::from);
    }

    @PutMapping("/{id}")
    public JobResponse update(@PathVariable UUID id, @Valid @RequestBody JobUpdateRequest request) {
        JobCommand command = new JobCommand(
                request.getTitle(), request.getDescription(), request.getDepartmentId(),
                null, request.getMinSalary(), request.getMaxSalary(), request.getDeadline(), request.getMaxApplicants());
        return JobResponse.from(updateJobUseCase.execute(id, command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteJobUseCase.execute(id);
    }

    private JobCommand toCommand(JobCreateRequest request) {
        return new JobCommand(
                request.getTitle(),
                request.getDescription(),
                request.getDepartmentId(),
                request.getRequiredSkills(),
                request.getMinSalary(),
                request.getMaxSalary(),
                request.getDeadline(),
                request.getMaxApplicants());
    }
}
