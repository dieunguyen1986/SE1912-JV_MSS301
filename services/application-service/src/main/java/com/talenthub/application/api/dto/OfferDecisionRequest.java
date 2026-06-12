package com.talenthub.application.api.dto;

import com.talenthub.application.application.usecase.DecideOfferUseCase;
import jakarta.validation.constraints.NotNull;

public record OfferDecisionRequest(@NotNull DecideOfferUseCase.Decision decision) {}
