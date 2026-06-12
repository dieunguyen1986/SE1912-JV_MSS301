package com.talenthub.application.api.dto;

import com.talenthub.application.domain.model.PipelineStage;
import jakarta.validation.constraints.NotNull;

public record AdvanceStageRequest(@NotNull PipelineStage targetStage) {}