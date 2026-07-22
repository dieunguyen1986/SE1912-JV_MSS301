package com.talenthub.events;

import java.util.UUID;

public record CVUploadedEvent(
        UUID eventId,
        UUID applicationId,
        UUID candidateId,
        String cvFileUrl        // URL file CV tren MinIO
) {
}
