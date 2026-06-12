package com.talenthub.application.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddNoteRequest(
        @NotBlank String author,
        @NotBlank String content
) {}
