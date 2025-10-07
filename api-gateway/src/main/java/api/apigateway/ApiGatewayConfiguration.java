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
                .build();
    }
}
