package org.example.orderservice.service;

import org.example.orderservice.dto.ProductResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ProductService {

    private final RestClient restClient;

    public ProductService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ProductResponse getProduct(Long productId) {
        return restClient.get()
                .uri("/products/{id}", productId)
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .retrieve()
                .body(ProductResponse.class);
    }

    public ProductResponse getProductByName(String productName) {
        return restClient.get()
                .uri("/products/{name}", productName)
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .retrieve()
                .body(ProductResponse.class);
    }

    private String getAuthHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        }
        return null;
    }

}
