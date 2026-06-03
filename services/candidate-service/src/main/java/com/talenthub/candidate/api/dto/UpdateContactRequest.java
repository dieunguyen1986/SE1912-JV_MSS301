package com.talenthub.candidate.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateContactRequest(
        @NotBlank @Email String email,
        String phone,
        String address
) {
}
