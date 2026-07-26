package com.talenthub.job.web;

import com.talenthub.job.api.dto.CustomUserDetails;
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
public class JobServiceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String correlationId = request.getHeader("X-Correlation-ID");

        log.info("requestURI={}, correlationId={}", requestURI, correlationId);

        String userId = request.getHeader("X-User-ID");
        String email = request.getHeader("X-User-Name");
        String rolesAsString = request.getHeader("X-User-Roles");

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
        
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(new CustomUserDetails(userId, email), null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
