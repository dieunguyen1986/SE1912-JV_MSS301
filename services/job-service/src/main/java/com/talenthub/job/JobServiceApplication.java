package com.talenthub.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JobServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobServiceApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () ->
//                Optional.ofNullable(
//                        SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getName)
//                .or(() ->
                Optional.of("system");
    }

}
