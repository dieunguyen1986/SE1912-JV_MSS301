package com.talenthub.application.infrastructure.client.job;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "job-service", configuration = JobFeignConfig.class, fallbackFactory = JobClientFallbackFactory.class)
public interface JobServiceClient {

    @GetMapping("/api/v1/jobs/{id}")
    JobView getJobById(@PathVariable("id") UUID id);

}
