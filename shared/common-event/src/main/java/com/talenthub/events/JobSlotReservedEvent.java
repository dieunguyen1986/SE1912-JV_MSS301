package com.talenthub.events;

import java.util.UUID;

public record JobSlotReservedEvent(
        UUID applicationId,
        UUID jobId,
        UUID candidateId,
        String candidateEmail,
        String candidateFullName,
        String cvFileUrl,
        String jobTitle
//        int currentCount,
//        int maxApplicants
) {
}
