package com.talenthub.application.infrastructure.client.candidate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "candidate-service",   // KHÔNG có url -> Feign resolve qua Eureka + LoadBalancer (lb://candidate-service)
        configuration = CandidateFeignConfig.class,
        fallbackFactory = CandidateClientFallbackFactory.class
)
public interface CandidateServiceClient {

    @GetMapping("/api/v1/candidates/{id}")
    CandidateView getCandidateById(@PathVariable("id") UUID candidateId);

}
