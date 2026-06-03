package com.talenthub.job.api;

import com.talenthub.job.api.dto.JobResponse;
import com.talenthub.job.api.dto.PageResponse;
import com.talenthub.job.application.usecase.GetJobUseCase;
import com.talenthub.job.application.usecase.ListJobsUseCase;
import com.talenthub.job.domain.Job;
import com.talenthub.job.domain.exception.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Public route cho Career Site — chỉ trả các job đã PUBLISHED.
 * Khớp Stage 3 route id `job-service-public` (Path=/api/v1/public/jobs/**).
 */
@RestController
@RequestMapping(ApiPath.PUBLIC_JOBS)
@RequiredArgsConstructor
public class PublicJobController {

    private final ListJobsUseCase listJobsUseCase;
    private final GetJobUseCase getJobUseCase;

    @GetMapping
    public PageResponse<JobResponse> listPublished(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
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
