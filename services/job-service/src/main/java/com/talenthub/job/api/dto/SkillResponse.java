package com.talenthub.job.api.dto;

import com.talenthub.job.domain.Skill;

import java.util.UUID;

public record SkillResponse(UUID id, String name, Skill.Type type) {
    public static SkillResponse from(Skill s) {
        return new SkillResponse(s.getId(), s.getName(), s.getType());
    }
}
