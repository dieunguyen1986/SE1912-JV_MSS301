package com.talenthub.application.infrastructure.client.job;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class JobFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder(){
        return new JobClientErrorDecoder();
    }
}
