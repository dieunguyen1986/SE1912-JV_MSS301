package com.talenthub.job.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Đóng dấu port của instance này vào mọi response header (X-Served-By),
 * giúp xác định request được instance job-service nào xử lý.
 */
@Component
public class ServedByFilter extends OncePerRequestFilter {

    private final Environment environment;

    @Value("${server.port:unknown}")
    private String configuredPort;

    public ServedByFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // local.server.port là port thực tế đang lắng nghe (kể cả khi cấu hình port = 0)
        String port = environment.getProperty("local.server.port", configuredPort);
        response.setHeader("X-Served-By", "job-service:" + port);
        filterChain.doFilter(request, response);
    }
}
