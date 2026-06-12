package com.talenthub.candidate.api.dto;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.model.ParsedCvData;

import java.time.Instant;
import java.util.UUID;

public record CandidateResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        String address,
        CvFileResponse cv,
        ParsedCvData parsed,
        Instant createdAt,
        Instant updatedAt
) {
    public static CandidateResponse from(Candidate c) {
        return new CandidateResponse(
                c.getId(),
                c.getFullName(),
                c.getContact() == null ? null : c.getContact().email(),
                c.getContact() == null ? null : c.getContact().phone(),
                c.getContact() == null ? null : c.getContact().address(),
                c.getCv() == null ? null : CvFileResponse.from(c.getCv()),
                c.getParsed(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
