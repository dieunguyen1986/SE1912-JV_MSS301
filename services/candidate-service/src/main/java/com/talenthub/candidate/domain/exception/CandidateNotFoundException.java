package com.talenthub.candidate.domain.exception;

import java.util.UUID;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(UUID id) {
        super("Candidate không tồn tại: " + id);
    }

    public CandidateNotFoundException(String email) {
        super("Candidate không tồn tại với email: " + email);
    }
}
