package com.talenthub.application.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExtendOfferRequest(
        @NotNull @Positive BigDecimal salary,
        @NotNull @Future LocalDate startDate
) {}
