package com.talenthub.job.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JobServiceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String header = request.getHeader("X-Correlation-ID");

        log.info("requestURI={}, header={}", requestURI, header);

        String userId = request.getHeader("X-User-ID");
        String email = request.getHeader("X-User-Email");

        // Store Security Context

        filterChain.doFilter(request, response);
    }
}
