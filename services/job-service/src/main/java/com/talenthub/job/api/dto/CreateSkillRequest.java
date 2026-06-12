package com.talenthub.job.api.dto;

import com.talenthub.job.domain.model.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSkillRequest(@NotBlank String name, @NotNull Skill.Type type) {
}
