package com.cjs.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author chengjiansheng
 */
@RestController
@SpringBootApplication
public class CjsGatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CjsGatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.path("/price/**")
                        .filters(f -> f.addRequestHeader("hello", "world")
                                .addRequestParameter("name", "zhangsan")
                                .requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("http://10.0.29.232:8082/price"))
                .route("path_route", r -> r.path("/commodity/**").uri("http://10.0.29.92:8081/commodity"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(2, 4);
    }

    @Bean
    KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("userId"));
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

}
