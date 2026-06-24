package com.talenthub.candidate.domain.exception;

import java.util.UUID;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(UUID id) {
        super("Không tìm thấy candidate: " + id);
    }
}
