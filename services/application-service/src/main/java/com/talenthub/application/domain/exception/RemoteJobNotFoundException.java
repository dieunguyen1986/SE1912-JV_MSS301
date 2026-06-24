package com.talenthub.application.domain.exception;

import java.util.UUID;

public class RemoteJobNotFoundException extends RuntimeException {
    public RemoteJobNotFoundException(UUID jobId) {
        super("Job " + jobId + " không tồn tại ở job-service");
    }

    public  RemoteJobNotFoundException(String message) {
        super(message);
    }
}