package com.talenthub.application.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader("X-User-ID");
            String userRoles = request.getHeader("X-User-Roles");
            String userName = request.getHeader("X-User-Name");
            String correlationId = request.getHeader("X-Correlation-ID");

            if (userId != null) {
                requestTemplate.header("X-User-ID", userId);
            }
            if (userRoles != null) {
                requestTemplate.header("X-User-Roles", userRoles);
            }
            if (userName != null) {
                requestTemplate.header("X-User-Name", userName);
            }
            if (correlationId != null) {
                requestTemplate.header("X-Correlation-ID", correlationId);
            }
        }
    }
}
