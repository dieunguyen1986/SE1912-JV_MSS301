package com.talenthub.candidate.domain;

import java.time.LocalDate;

/**
 * Value Object — kinh nghiệm làm việc.
 * to = null → đang làm hiện tại.
 */
public record WorkExperience(
        String company,
        String position,
        LocalDate from,
        LocalDate to
) {
    public WorkExperience {
        if (company == null || company.isBlank()) {
            throw new IllegalArgumentException("company required");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("position required");
        }
        if (from == null) {
            throw new IllegalArgumentException("from required");
        }
        if (to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from phải <= to");
        }
        company = company.trim();
        position = position.trim();
    }
}
