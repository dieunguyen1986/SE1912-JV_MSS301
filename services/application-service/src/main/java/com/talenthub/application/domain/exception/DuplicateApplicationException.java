package com.talenthub.application.domain.exception;

import java.util.UUID;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException(UUID candidateId, UUID jobId) {
        super("Candidate " + candidateId + " đã nộp đơn job " + jobId + " rồi (BRULE-09)");
    }
}