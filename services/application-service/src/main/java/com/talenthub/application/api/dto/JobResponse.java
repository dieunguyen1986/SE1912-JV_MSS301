package com.talenthub.application.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String title,
        String description,
        String location,
        UUID departmentId,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        LocalDate deadline,
        String status,
        Set<String> requiredSkills,
        Instant createdAt,
        Instant updatedAt
){}