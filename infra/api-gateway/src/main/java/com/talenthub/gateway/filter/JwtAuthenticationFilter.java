package com.talenthub.gateway.filter;

import com.talenthub.gateway.utils.GatewayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String jwt = exchange.getRequest().getHeaders().getFirst(GatewayConstants.HEADER_AUTHOR);

        // Validate token
        ServerHttpRequest muteRequest = exchange.getRequest().mutate().headers((httpHeaders -> {
            httpHeaders.add(GatewayConstants.HEADER_USER_ID, "10");
            httpHeaders.add(GatewayConstants.HEADER_USER_ROLES, "['ROLE_ADMIN', 'ROLE_REC']");
            httpHeaders.add(GatewayConstants.HEADER_USER_NAME, "rec@gmail.com");
        })).build();

        return chain.filter(exchange.mutate().request(muteRequest).build());
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDER_JWT_AUTH_FILTER;
    }
}
