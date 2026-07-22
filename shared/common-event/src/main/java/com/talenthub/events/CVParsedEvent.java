package com.talenthub.events;

import java.util.UUID;

public record CVParsedEvent(
        UUID applicationId,
        String candidateEmail,
        String candidateFullName
) {
}
