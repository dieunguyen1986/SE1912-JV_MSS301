package com.talenthub.application.application.command;

import java.util.UUID;

public record SubmitApplicationCommand(UUID candidateId, UUID jobId) {
}