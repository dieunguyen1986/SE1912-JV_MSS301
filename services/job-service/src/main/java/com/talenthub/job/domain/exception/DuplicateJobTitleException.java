package com.talenthub.job.domain.exception;

public class DuplicateJobTitleException extends RuntimeException {
    public DuplicateJobTitleException(String title) {
        super("Job title already exists: " + title);
    }
}
