package com.gym.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;


@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("trainer-service", r -> r.path("/api/trainers/**")
                        .uri("http://localhost:8030"))
                .route("equipment-service", r -> r.path("/api/equipment/**")
                        .uri("http://localhost:8040"))
                .route("member-service", r -> r.path("/api/members/**")
                        .uri("http://localhost:8050"))
                .route("classes-service", r -> r.path("/api/classes/**")
                        .uri("http://localhost:8060"))
                .build();
    }
    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                return chain.filter(exchange.mutate()
                        .request(request.mutate()
                                .header(HttpHeaders.AUTHORIZATION, authHeader)
                                .build())
                        .build());
            }
            return chain.filter(exchange);
        };
    }
}