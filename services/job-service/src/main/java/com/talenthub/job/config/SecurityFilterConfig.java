package com.talenthub.job.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JobServiceFilter jobServiceFilter) throws Exception {
        return http.csrf((csrf) -> {
                    csrf.disable();
                })
                .authorizeHttpRequests((authorize) -> {
                    authorize.anyRequest().permitAll();
                })
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jobServiceFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
