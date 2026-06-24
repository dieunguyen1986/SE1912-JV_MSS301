package com.talenthub.application.infrastructure.client.job;

import com.talenthub.application.domain.exception.RemoteJobNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class JobClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 404 -> new RemoteJobNotFoundException("Job không tồn tại: " + response.request().url());
            // 5xx / status khác thì để Feign sinh FeignException như cũ
            // GlobalExceptionHandler.handleFeign map thành 502

            default -> defaultErrorDecoder.decode(s, response);
        };

    }
}
