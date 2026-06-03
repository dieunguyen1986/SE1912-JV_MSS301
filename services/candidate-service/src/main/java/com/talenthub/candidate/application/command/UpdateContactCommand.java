package com.talenthub.candidate.application.command;

import java.util.UUID;

public record UpdateContactCommand(UUID candidateId, String email, String phone, String address) {
}
