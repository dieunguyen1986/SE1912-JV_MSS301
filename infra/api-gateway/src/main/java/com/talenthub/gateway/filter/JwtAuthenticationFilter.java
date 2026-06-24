package com.talenthub.gateway.filter;

import com.talenthub.gateway.utils.GatewayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap((jwtAuthenticationToken -> {

                    Jwt jwt = jwtAuthenticationToken.getToken();
                    String email = jwt.getClaimAsString("email");
                    String userId = jwt.getSubject();
                    List<String> roles = jwt.getClaimAsStringList("roles");

                    String rolesAsString = roles.stream().map((role) -> "ROLE_" + role).collect(Collectors.joining(","));

                    log.info("userId = {}, roles = {}", userId, rolesAsString);
                    // Enrich Header
                    // Validate token
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().headers((httpHeaders -> {
                        httpHeaders.add(GatewayConstants.HEADER_USER_ID, userId);
                        httpHeaders.add(GatewayConstants.HEADER_USER_ROLES, rolesAsString);
                        httpHeaders.add(GatewayConstants.HEADER_USER_NAME, email);
                    })).build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                }))
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDER_JWT_AUTH_FILTER;
    }
}
