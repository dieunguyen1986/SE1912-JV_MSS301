package com.talenthub.application.config;

import com.talenthub.application.api.dto.CustomUserDetails;
import com.talenthub.application.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ApplicationServiceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(Constants.HEADER_CORRELATION_ID);
        log.info("correlationId={}", correlationId);

        String userId = request.getHeader(Constants.HEADER_USER_ID);
        String email = request.getHeader(Constants.HEADER_USER_NAME);

        String rolesAsString = request.getHeader(Constants.HEADER_USER_ROLES);
        log.info("userId = {}, roles = {}", userId, rolesAsString);

        // Nếu không có header (request không qua Gateway, health check, actuator...),
        // bỏ qua authentication và tiếp tục filter chain
        if (userId == null || rolesAsString == null) {
            filterChain.doFilter(request, response);
            return;
        }

        List<GrantedAuthority> grantedAuthorities = Arrays.stream(rolesAsString.split(","))
                .map(role -> {
                    String r = role.trim();
                    if (!r.startsWith("ROLE_")) {
                        r = "ROLE_" + r;
                    }
                    return new SimpleGrantedAuthority(r);
                }).collect(Collectors.toList());
        log.info("grantedAuthorities = {}", grantedAuthorities);
        // Add user to context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(userId, email), null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
