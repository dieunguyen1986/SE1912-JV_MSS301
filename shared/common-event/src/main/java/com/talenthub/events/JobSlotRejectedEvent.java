package com.talenthub.events;

import java.util.UUID;

public record JobSlotRejectedEvent(
        UUID applicationId,
        UUID jobId,
        UUID candidateId,
        String candidateEmail,
        String candidateFullName,
        String reason
) {
}
