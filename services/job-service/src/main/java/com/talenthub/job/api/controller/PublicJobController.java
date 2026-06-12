package com.talenthub.job.api.controller;

import com.talenthub.job.api.dto.JobResponse;
import com.talenthub.job.api.dto.PageResponse;
import com.talenthub.job.application.usecase.GetJobUseCase;
import com.talenthub.job.application.usecase.ListJobsUseCase;
import com.talenthub.job.domain.exception.JobNotFoundException;
import com.talenthub.job.domain.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Public route cho Career Site — chỉ trả các job đã PUBLISHED.
 */
@RestController
@RequestMapping(ApiPath.PUBLIC_JOBS)
@RequiredArgsConstructor
@Slf4j
public class PublicJobController {

    private final ListJobsUseCase listJobsUseCase;
    private final GetJobUseCase getJobUseCase;

    @GetMapping
    public PageResponse<JobResponse> listPublished(@RequestHeader(name = "X-Correlation-ID") String correlationId,
                                                   @RequestParam(name = "keyword", required = false) String keyword,
                                                   @PageableDefault(size = 20) Pageable pageable,
                                                   Authentication authentication
    ) {
        log.info("Correlation ID {}", correlationId);
        log.info("Keyword {}", keyword);
        log.info("Page {}", pageable);
        log.info("Page size {}", pageable.getPageSize());
        log.info("Authentication {}", ((User) authentication.getDetails()).getUsername());

        Page<Job> page = listJobsUseCase.published(keyword, pageable);
        return PageResponse.of(page, JobResponse::from);
    }

    @GetMapping(ApiPath.BY_ID)
    public JobResponse getPublishedById(@PathVariable UUID id) {
        Job job = getJobUseCase.execute(id);
        if (job.getStatus() != Job.Status.PUBLISHED) {
            throw new JobNotFoundException(id);
        }
        return JobResponse.from(job);
    }

}
