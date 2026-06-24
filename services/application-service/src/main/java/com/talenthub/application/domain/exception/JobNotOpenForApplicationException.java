package com.talenthub.application.domain.exception;


import java.util.UUID;

/**
 * Job tồn tại nhưng không nhận đơn: chưa PUBLISHED hoặc đã quá hạn.
 */
public class JobNotOpenForApplicationException extends RuntimeException {
    public JobNotOpenForApplicationException(UUID jobId, String reason) {
        super("Không thể nộp đơn cho job " + jobId + ": " + reason);
    }
}