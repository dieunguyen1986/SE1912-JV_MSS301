package com.talenthub.notification.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class PingController {
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "service", "notification-service",
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
    }
}
