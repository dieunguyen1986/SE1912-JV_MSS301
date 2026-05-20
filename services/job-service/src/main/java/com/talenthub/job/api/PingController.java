package com.talenthub.job.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/jobs")
public class PingController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "ok",
                "service", "job-service",
                "message", "Job Service Hello World!"
        );
    }

}
