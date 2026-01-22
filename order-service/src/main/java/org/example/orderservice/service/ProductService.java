package org.example.orderservice.service;

import org.example.orderservice.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    public ProductResponse getProduct(Long productId) {
        try {
            String url = "http://localhost:8765/products/" + productId;
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String authHeader = null;
            if (attributes != null) {
                authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }

            HttpHeaders headers = new HttpHeaders();
            if (authHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ProductResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProductResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch product with name: " + productId, e);
        }
    }

    public ProductResponse getProductByName(String productName) {
        try {
            String url = "http://localhost:8765/products/" + productName;

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String authHeader = null;
            if (attributes != null) {
                authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }

            HttpHeaders headers = new HttpHeaders();
            if (authHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ProductResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ProductResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch product with name: " + productName, e);
        }
    }
}
