package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.JobResponse;
import com.talenthub.job.api.dto.PageResponse;
import com.talenthub.job.application.usecase.GetJobByIdUseCase;
import com.talenthub.job.application.usecase.SearchJobsUseCase;
import com.talenthub.job.domain.aggregate.JobAggregate;
import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.infrastructure.specification.JobSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/jobs")
@RequiredArgsConstructor
public class PublicJobController {

    private final SearchJobsUseCase searchJobsUseCase;
    private final GetJobByIdUseCase getJobByIdUseCase;

    @GetMapping
    public PageResponse<JobResponse> listPublished(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        JobSearchCriteria criteria = JobSearchCriteria.builder()
                .keyword(keyword)
                .status(JobAggregate.Status.PUBLISHED)
                .build();
        Page<JobAggregate> page = searchJobsUseCase.execute(criteria, pageable);
        return PageResponse.of(page, JobResponse::from);
    }

    @GetMapping("/{id}")
    public JobResponse getPublished(@PathVariable UUID id) {
        JobAggregate job = getJobByIdUseCase.execute(id);
        if (job.getStatus() != JobAggregate.Status.PUBLISHED) {
            throw new JobNotFoundException(id);
        }
        return JobResponse.from(job);
    }
}
