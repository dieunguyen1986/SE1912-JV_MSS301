package com.talenthub.candidate.api.dto;

import com.talenthub.candidate.domain.CvFile;

import java.time.Instant;
import java.util.UUID;

public record CvFileResponse(
        UUID id,
        String fileUrl,
        long sizeBytes,
        Instant uploadedAt,
        CvFile.ParseStatus parseStatus
) {
    public static CvFileResponse from(CvFile cv) {
        return new CvFileResponse(
                cv.getId(),
                cv.getFileUrl(),
                cv.getSizeBytes(),
                cv.getUploadedAt(),
                cv.getParseStatus()
        );
    }
}
