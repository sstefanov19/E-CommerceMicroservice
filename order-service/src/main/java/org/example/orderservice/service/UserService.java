package org.example.orderservice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public BigDecimal getUserBalance() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String token = (String) authentication.getCredentials();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization" , "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = "http://localhost:8100/auth/balance";
            return restTemplate.exchange(url, HttpMethod.GET , entity, BigDecimal.class).getBody();
        } catch (HttpServerErrorException e) {
            System.err.println("Products service error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch user balance: ", e);
        }
    }


}
