package com.talenthub.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class CandidateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandidateServiceApplication.class, args);
    }


    @Bean
    public AuditorAware<String> auditorAware() {
        return () ->
                //Optional.ofNullable(
//                        SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getName)
//                .or(() ->

                Optional.of("system");   // fallback khi chưa có Security (L4-L11)
    }


}
