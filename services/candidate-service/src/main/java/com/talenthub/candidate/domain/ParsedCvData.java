package com.talenthub.candidate.domain;

import java.util.List;

/**
 * Value Object — kết quả parse CV (từ cv-parser-service trả về).
 * Defensive copy 3 List qua List.copyOf() để giữ immutability.
 */
public record ParsedCvData(
        List<String> skills,
        List<Education> educations,
        List<WorkExperience> experiences,
        int totalYearsExperience
) {
    public ParsedCvData {
        if (totalYearsExperience < 0 || totalYearsExperience > 70) {
            throw new IllegalArgumentException("totalYearsExperience phải 0-70");
        }
        skills = skills == null ? List.of() : List.copyOf(skills);
        educations = educations == null ? List.of() : List.copyOf(educations);
        experiences = experiences == null ? List.of() : List.copyOf(experiences);
    }
}
