package com.talenthub.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component

public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Pre-filter
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String correlationId = serverHttpRequest.getHeaders().getFirst("X-Request-ID");

        log.info("Request header {}", correlationId);
        // Check header
        if(correlationId == null) {
            correlationId = UUID.randomUUID().toString().substring(0, 8);
        }


        // Post-filter
        return (Mono<Void>) exchange.getRequest().mutate().header("X-Request-ID", correlationId).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
