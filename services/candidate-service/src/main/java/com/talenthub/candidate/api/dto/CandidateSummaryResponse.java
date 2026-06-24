package com.talenthub.candidate.api.dto;

import com.talenthub.candidate.domain.model.Candidate;

import java.time.Instant;
import java.util.UUID;

public record CandidateSummaryResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        boolean hasCv,
        Instant createdAt
) {
    public static CandidateSummaryResponse from(Candidate c) {
        return new CandidateSummaryResponse(
                c.getId(),
                c.getFullName(),
                c.getContact().email(),
                c.getContact().phone(),
                c.getCv() != null,
                c.getCreatedAt()
        );
    }
}
