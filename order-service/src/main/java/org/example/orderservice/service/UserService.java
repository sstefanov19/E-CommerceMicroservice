package org.example.orderservice.service;

import org.example.orderservice.dto.UpdateBalanceRequest;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Long getUserId() {
        try {
            String url = "http://localhost:8765/auth/validate";
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

            ResponseEntity<Long> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Long.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch id for user", e);
        }
    }


    public BigDecimal getUserBalance() {
        try {
            String url = "http://localhost:8765/auth/balance";
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

            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    BigDecimal.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch balance for user", e);
        }
    }

    public void updateUserBalance(UpdateBalanceRequest request) {
        try {
            String url = "http://localhost:8765/auth/balance/update";
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String authHeader = null;
            if (attributes != null) {
                authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            }

            HttpHeaders headers = new HttpHeaders();
            if (authHeader != null) {
                headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            }
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            Map<String, BigDecimal> body = Collections.singletonMap("cost", request.getCost());

            HttpEntity<Map<String, BigDecimal>> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to update balance for user", e);
        }
    }

}
