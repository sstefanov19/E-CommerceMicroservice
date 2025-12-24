package org.example.orderservice.service;

import org.example.orderservice.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    public ProductResponse getProduct(Long productId) {
        try {
            String url = "http://localhost:8200/products/" + productId;
            return restTemplate.getForObject(url, ProductResponse.class);
        } catch (HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch product with ID: " + productId, e);
        }
    }

    public ProductResponse getProductByName(String productName) {
        try {
            String url = "http://localhost:8200/products/" + productName;
            return restTemplate.getForObject(url, ProductResponse.class);
        } catch (HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch product with name: " + productName, e);
        }
    }
}
