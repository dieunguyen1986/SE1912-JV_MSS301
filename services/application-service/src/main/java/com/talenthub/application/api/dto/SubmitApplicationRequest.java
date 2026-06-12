package com.talenthub.application.api.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubmitApplicationRequest(
        @NotNull UUID candidateId,
        @NotNull UUID jobId
) {}
