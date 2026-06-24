package com.talenthub.application.infrastructure.client.job;

import java.time.LocalDate;
import java.util.UUID;

public record JobView(
        UUID id,
        String title,
        JobStatus status,
        LocalDate deadline,
        UUID departmentId
) {
    public boolean isPublished() {
        return status == JobStatus.PUBLISHED;
    }

    public boolean isExprired(LocalDate today) {
        return deadline != null && today.isAfter(deadline);
    }
}

enum JobStatus {
    DRAFT, PENDING_APPROVAL, APPROVED, PUBLISHED, CLOSED;
}