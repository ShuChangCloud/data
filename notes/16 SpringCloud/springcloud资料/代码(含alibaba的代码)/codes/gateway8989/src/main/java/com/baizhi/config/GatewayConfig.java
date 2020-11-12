package com.baizhi.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("user_route",r->r.path("/feign/**").uri("http://localhost:9999/"))
//                .route("product_route",r->r.path("/product/**").uri("http://localhost:9998/"))
//                .build();
//    }
//}