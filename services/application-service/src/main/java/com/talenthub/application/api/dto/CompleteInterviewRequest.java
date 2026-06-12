package com.talenthub.application.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CompleteInterviewRequest(
        @Min(1) @Max(10) int score,
        String comment,
        @NotBlank String interviewerName
) {}
