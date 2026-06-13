package com.talenthub.job.config;

import com.talenthub.job.domain.model.CustomUserDetails;
import com.talenthub.job.utils.Constants;
import com.talenthub.job.utils.UserUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JobServiceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(Constants.HEADER_CORRELATION_ID);
        String userId = request.getHeader(Constants.HEADER_USER_ID);
        String username = request.getHeader(Constants.HEADER_USER_NAME);
        String userRoles = request.getHeader(Constants.HEADER_USER_ROLES);

        log.info("Filter - Correlation ID {}", correlationId);
        log.info("Filter - User ID {}", userId);
        log.info("Filter - User name {}", username);
        log.info("Filter - Roles {}", userRoles);

        if (userId == null || username == null || userRoles == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        List<GrantedAuthority> grantedAuthorities = UserUtils.extractRole(userRoles);

        // Store to security context
        UserDetails userDetails = new CustomUserDetails(userId, username, grantedAuthorities);

        // Store to context
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
