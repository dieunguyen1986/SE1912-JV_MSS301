package com.talenthub.job.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record UpdateJobRequest(
        @NotBlank String title,
        String description,
        String location,
        @NotEmpty Set<String> requiredSkills,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal minSalary,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal maxSalary,
        LocalDate deadline
) {
}
