package api.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator gatewayConfiguration(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/products/**")
                        .filters(f -> f.filter(filter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://PRODUCTS"))
                .route(p -> p.path("/orders/**")
                        .filters(f -> f.filter(filter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://ORDERS"))
                .route(p -> p.path("/auth/balance")
                        .filters(f -> f.filter(filter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://AUTH"))
                .route(p -> p.path("/auth/**")
                        .uri("lb://AUTH"))
                .build();
    }
}
