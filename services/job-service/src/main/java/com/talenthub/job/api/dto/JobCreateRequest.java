package com.talenthub.job.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JobCreateRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    private UUID departmentId;

    private Set<String> requiredSkills = new HashSet<>();

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal minSalary;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal maxSalary;

    @NotNull
    @Future
    private LocalDate deadline;
}
