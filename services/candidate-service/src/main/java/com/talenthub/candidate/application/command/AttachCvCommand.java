package com.talenthub.candidate.application.command;

import java.util.UUID;

public record AttachCvCommand(UUID candidateId, String fileUrl, long sizeBytes) {
}
