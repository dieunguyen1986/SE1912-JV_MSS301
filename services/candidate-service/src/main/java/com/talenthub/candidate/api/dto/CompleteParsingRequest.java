package com.talenthub.candidate.api.dto;

import com.talenthub.candidate.domain.Education;
import com.talenthub.candidate.domain.WorkExperience;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CompleteParsingRequest(
        @NotNull List<String> skills,
        @NotNull List<Education> educations,
        @NotNull List<WorkExperience> experiences,
        @Min(0) int totalYearsExperience
) {
}
