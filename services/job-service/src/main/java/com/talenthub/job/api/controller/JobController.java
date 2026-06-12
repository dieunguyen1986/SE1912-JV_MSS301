package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.JobCreatedRequest;
import com.talenthub.job.api.dto.JobResponse;
import com.talenthub.job.api.dto.PageResponse;
import com.talenthub.job.api.dto.UpdateJobRequest;
import com.talenthub.job.application.command.DraftJobCommand;
import com.talenthub.job.application.command.UpdateJobCommand;
import com.talenthub.job.application.usecase.ApproveJobUseCase;
import com.talenthub.job.application.usecase.CloseJobUseCase;
import com.talenthub.job.application.usecase.DeleteJobUseCase;
import com.talenthub.job.application.usecase.DraftJobUseCase;
import com.talenthub.job.application.usecase.GetJobUseCase;
import com.talenthub.job.application.usecase.ListJobsUseCase;
import com.talenthub.job.application.usecase.PublishJobUseCase;
import com.talenthub.job.application.usecase.SubmitJobUseCase;
import com.talenthub.job.application.usecase.UpdateJobUseCase;
import com.talenthub.job.domain.model.Job;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(ApiPath.JOBS)
@RequiredArgsConstructor
public class JobController {

    private final DraftJobUseCase draftJobUseCase;
    private final GetJobUseCase getJobUseCase;
    private final ListJobsUseCase listJobsUseCase;
    private final UpdateJobUseCase updateJobUseCase;
    private final SubmitJobUseCase submitJobUseCase;
    private final ApproveJobUseCase approveJobUseCase;
    private final PublishJobUseCase publishJobUseCase;
    private final CloseJobUseCase closeJobUseCase;
    private final DeleteJobUseCase deleteJobUseCase;

    @PostMapping
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobCreatedRequest req) {
        Job job = draftJobUseCase.execute(new DraftJobCommand(
                req.title(), req.description(), req.location(), req.departmentId(),
                req.requiredSkills(), req.minSalary(), req.maxSalary(), req.deadline()));
        return ResponseEntity
                .created(URI.create(ApiPath.JOBS + "/" + job.getId()))
                .body(JobResponse.from(job));
    }

    @GetMapping(ApiPath.BY_ID)
    public JobResponse getById(@PathVariable UUID id) {
        return JobResponse.from(getJobUseCase.execute(id));
    }

    @GetMapping
    public PageResponse<JobResponse> list(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Job> page = listJobsUseCase.all(keyword, pageable);
        return PageResponse.of(page, JobResponse::from);
    }

    @PutMapping(ApiPath.BY_ID)
    public JobResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateJobRequest req) {
        Job job = updateJobUseCase.execute(new UpdateJobCommand(
                id, req.title(), req.description(), req.location(),
                req.requiredSkills(), req.minSalary(), req.maxSalary(), req.deadline()));
        return JobResponse.from(job);
    }

    @PutMapping(ApiPath.SUBMIT)
    public JobResponse submit(@PathVariable UUID id) {
        return JobResponse.from(submitJobUseCase.execute(id));
    }

    @PutMapping(ApiPath.APPROVE)
    public JobResponse approve(@PathVariable UUID id) {
        return JobResponse.from(approveJobUseCase.execute(id));
    }

    @PutMapping(ApiPath.PUBLISH)
    public JobResponse publish(@PathVariable UUID id) {
        return JobResponse.from(publishJobUseCase.execute(id));
    }

    @PutMapping(ApiPath.CLOSE)
    public JobResponse close(@PathVariable UUID id) {
        return JobResponse.from(closeJobUseCase.execute(id));
    }

    @DeleteMapping(ApiPath.BY_ID)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteJobUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
