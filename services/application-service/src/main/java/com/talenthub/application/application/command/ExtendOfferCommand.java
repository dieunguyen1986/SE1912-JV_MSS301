package com.talenthub.application.application.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExtendOfferCommand(UUID applicationId, BigDecimal salary, LocalDate startDate) {
}
