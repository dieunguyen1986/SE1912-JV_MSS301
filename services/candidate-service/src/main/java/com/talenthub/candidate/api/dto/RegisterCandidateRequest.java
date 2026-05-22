package com.talenthub.candidate.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCandidateRequest(@NotBlank String fullName, @NotBlank @Email String email, String phone, String address) {
}
