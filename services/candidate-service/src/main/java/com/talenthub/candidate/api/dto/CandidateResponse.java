package com.talenthub.candidate.api.dto;

import com.talenthub.candidate.domain.model.Candidate;
import com.talenthub.candidate.domain.model.CvFile;
import com.talenthub.candidate.domain.model.ParsedCvData;

import java.time.Instant;
import java.util.UUID;

public record CandidateResponse(
        UUID id,
        String fullName,
        ContactDto contact,
        CvFileDto cv,
        ParsedCvData parsed,
        Instant createdAt,
        Instant updatedAt
) {
    public record ContactDto(String email, String phone, String address) {}

    public record CvFileDto(UUID id, String fileUrl, long sizeBytes, Instant uploadedAt, String parseStatus) {}

    public static CandidateResponse from(Candidate c) {
        return new CandidateResponse(
                c.getId(),
                c.getFullName(),
                new ContactDto(c.getContact().email(), c.getContact().phone(), c.getContact().address()),
                c.getCv() == null ? null : toCvDto(c.getCv()),
                c.getParsed(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    private static CvFileDto toCvDto(CvFile cv) {
        return new CvFileDto(cv.getId(), cv.getFileUrl(), cv.getSizeBytes(),
                cv.getUploadedAt(), cv.getParseStatus().name());
    }
}
