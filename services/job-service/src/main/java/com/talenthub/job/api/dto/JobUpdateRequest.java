package com.talenthub.job.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JobUpdateRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    private UUID departmentId;

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
