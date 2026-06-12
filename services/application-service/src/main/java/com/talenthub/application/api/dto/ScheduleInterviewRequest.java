package com.talenthub.application.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleInterviewRequest(
        @NotNull @Future LocalDateTime scheduledAt,
        @NotBlank String interviewerName
) {}
