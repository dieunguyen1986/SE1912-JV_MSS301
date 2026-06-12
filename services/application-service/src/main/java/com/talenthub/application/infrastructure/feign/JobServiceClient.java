package com.talenthub.application.infrastructure.feign;

import com.talenthub.application.api.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "job-service", url = "http://localhost:8081")
public interface JobServiceClient {

    @GetMapping("/api/v1/jobs/{id}")
    JobResponse findById(@PathVariable(name = "id") UUID jobId);
}
