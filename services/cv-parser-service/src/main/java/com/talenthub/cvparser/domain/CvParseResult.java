package com.talenthub.cvparser.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CvParseResult {

    public enum Status {PENDING, PARSED, FAILED}

    private UUID id;
    private UUID candidateId;
    private String cvFileUrl;
    private String rawText;
    private Status status;
    private String errorMessage;
    private Instant createdAt;
    private Instant completedAt;

    public static CvParseResult create(UUID candidateId, String cvFileUrl) {
        if (candidateId == null || cvFileUrl == null) {
            throw new IllegalArgumentException("candidateId and cvFileUrl required");
        }
        CvParseResult r = new CvParseResult();
        r.id = UUID.randomUUID();
        r.candidateId = candidateId;
        r.cvFileUrl = cvFileUrl;
        r.status = Status.PENDING;
        r.createdAt = Instant.now();
        return r;
    }

    public void markParsed(String rawText) {
        this.rawText = rawText;
        this.status = Status.PARSED;
        this.completedAt = Instant.now();
    }

    public void markFailed(String errorMessage) {
        this.errorMessage = errorMessage;
        this.status = Status.FAILED;
        this.completedAt = Instant.now();
    }
}