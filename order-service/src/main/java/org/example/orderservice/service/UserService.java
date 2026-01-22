package org.example.orderservice.service;

import org.example.orderservice.dto.UpdateBalanceRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService {

    private final RestClient restClient;

    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Long getUserId() {
        return restClient.get()
                .uri("/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .retrieve()
                .body(Long.class);
    }

    public BigDecimal getUserBalance() {
        return restClient.get()
                .uri("/auth/balance")
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .retrieve()
                .body(BigDecimal.class);
    }

    public void updateUserBalance(UpdateBalanceRequest request) {
        Map<String, BigDecimal> body = Collections.singletonMap("cost", request.cost());

        restClient.post()
                .uri("/auth/balance/update")
                .header(HttpHeaders.AUTHORIZATION, getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private String getAuthHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        }
        return null;
    }

}
