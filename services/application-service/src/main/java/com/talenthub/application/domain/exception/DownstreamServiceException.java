package com.talenthub.application.domain.exception;

public class DownstreamServiceException extends RuntimeException {
    public DownstreamServiceException(String serviceName) {
        super("Downstream service lỗi: " + serviceName);
    }
}
