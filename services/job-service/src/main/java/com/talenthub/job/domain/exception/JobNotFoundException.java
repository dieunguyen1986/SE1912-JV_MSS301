package com.talenthub.job.domain.exception;

import java.util.UUID;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(UUID id) {
        super("Job không tồn tại: " + id);
    }
}
