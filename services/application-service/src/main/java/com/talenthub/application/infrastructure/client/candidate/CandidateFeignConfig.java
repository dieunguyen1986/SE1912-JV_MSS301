package com.talenthub.application.infrastructure.client.candidate;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class CandidateFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CandidateClientErrorDecoder();
    }
}
