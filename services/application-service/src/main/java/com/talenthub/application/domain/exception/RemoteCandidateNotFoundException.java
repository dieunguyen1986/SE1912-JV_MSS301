package com.talenthub.application.domain.exception;

import java.util.UUID;

public class RemoteCandidateNotFoundException extends RuntimeException {
    public RemoteCandidateNotFoundException(UUID candidateId) {
        super("Candidate " + candidateId + " không tồn tại ở candidate-service");
    }

    public RemoteCandidateNotFoundException(String message) {
        super(message);
    }
}
