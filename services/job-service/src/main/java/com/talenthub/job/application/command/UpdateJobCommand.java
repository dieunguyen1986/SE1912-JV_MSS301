package com.talenthub.job.application.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UpdateJobCommand(
        UUID jobId,
        String title,
        String description,
        String location,
        Set<String> requiredSkills,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        LocalDate deadline
) {
}
