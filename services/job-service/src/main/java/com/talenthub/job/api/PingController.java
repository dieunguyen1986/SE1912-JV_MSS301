package com.talenthub.job.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class PingController {

    private final RestTemplate restTemplate;

    private static final String CANDIDATE_SERVICE_URL = "http://localhost:8082/api/v1/candidates/ping";

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "service", "job-service",
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/ping-candidate")
    public Map<String, Object> pingCandidate() {
        try {
            Map<?, ?> candidateResp = restTemplate.getForObject(CANDIDATE_SERVICE_URL, Map.class);

            return Map.of(
                    "self", "job-service",
                    "calledService", "candidate-service",
                    "remoteResponse", candidateResp,
                    "callMechanism", "RestTemplate & hardcode URL"
            );

        } catch (Exception e) {
            return Map.of(
                    "self", "job-service",
                    "error", "Candidate Service unreachable",
                    "cause", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            );
        }
    }
}
