package com.talenthub.candidate.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AttachCvRequest(
        @NotBlank String fileUrl,
        @Positive long sizeBytes
) {
}
