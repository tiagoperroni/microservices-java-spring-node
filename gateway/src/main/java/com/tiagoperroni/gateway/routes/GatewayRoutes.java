package com.tiagoperroni.gateway.routes;

import java.time.LocalDateTime;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutes {
    
    private LocalDateTime date = LocalDateTime.now();

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/clients/**")
            .filters(f -> f.rewritePath("/clients/(?<segment>.*)", "/${segment}")
            .addRequestHeader("X-Response-Time", date.toString()))
            .uri("lb://CLIENT"))

            .route(r -> r.path("/product/**")
            .filters(f -> f.rewritePath("/product/(?<segment>.*)", "/${segment}")
            .addRequestHeader("X-Response-Time", date.toString()))
            .uri("lb://PRODUCT-API"))

            // .route(r -> r.path("/authentication/**")
            // .filters(f -> f.rewritePath("/authentication/(?<segment>.*)", "/${segment}")
            // .addRequestHeader("X-Response-Time", date.toString()))
            // .uri("lb://AUTHENTICATION"))

            // .route(r -> r.path("/authenticationclient/**")
            // .filters(f -> f.rewritePath("/authenticationclient/(?<segment>.*)", "/${segment}")
            // .addRequestHeader("X-Response-Time", date.toString()))
            // .uri("lb://AUTHENTICATIONCLIENT"))

            .route(r -> r.path("/orders/**")
            .filters(f -> f.rewritePath("/orders/(?<segment>.*)", "/${segment}")
            .addRequestHeader("X-Response-Time", date.toString()))
            .uri("lb://ORDER-API")).build();
    }
}
