package com.talenthub.candidate.domain;

import java.time.Year;

/**
 * Value Object — học vấn.
 * Lưu trong ParsedCvData (List<Education>) — không phải @Embeddable đơn lẻ.
 */
public record Education(
        String school,
        String degree,
        int graduationYear
) {
    public Education {
        if (school == null || school.isBlank()) {
            throw new IllegalArgumentException("school required");
        }
        if (degree == null || degree.isBlank()) {
            throw new IllegalArgumentException("degree required");
        }
        int currentYear = Year.now().getValue();
        if (graduationYear < 1950 || graduationYear > currentYear + 10) {
            throw new IllegalArgumentException("graduationYear phải 1950 - " + (currentYear + 10));
        }
        school = school.trim();
        degree = degree.trim();
    }
}
