package com.talenthub.events;

import java.util.UUID;

public record CVParseFailedEvent(
        UUID applicationId,
        UUID jobId,
        String candidateEmail,
        String candidateFullName,
        String reason
) {
}
