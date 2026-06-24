package com.talenthub.candidate.domain.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email đã tồn tại: " + email);
    }
}
