package com.talenthub.job.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateDepartmentRequest(@NotBlank String name, UUID managerId) {
}
