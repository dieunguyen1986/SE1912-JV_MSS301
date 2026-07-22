package com.talenthub.events;

import java.util.UUID;

public record ApplicationSubmittedEvent(
        UUID applicationId,
        UUID jobId,
        UUID candidateId,
        String cvFileUrl,
        String candidateEmail,
        String candidateFullName
) {
}
