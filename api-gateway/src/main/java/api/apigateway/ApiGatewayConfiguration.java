package api.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {


    @Bean
    public RouteLocator gatewayConfiguration(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/products/**")
                        .uri("lb://products"))
                .route(p -> p.path("/auth/**")
                        .uri("lb://auth"))
                .route(p -> p.path("/orders/**")
                        .uri("lb://orders"))
                .build();
    }
}
