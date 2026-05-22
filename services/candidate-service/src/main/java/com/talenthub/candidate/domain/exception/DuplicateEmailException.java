package com.talenthub.candidate.domain.exception;

public class DuplicateEmailException extends  RuntimeException {
    public  DuplicateEmailException(String message) {
        super(message);
    }
}
