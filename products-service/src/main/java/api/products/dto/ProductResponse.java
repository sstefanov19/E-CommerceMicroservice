package api.products.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductResponse(String category , String name, BigDecimal price, Integer quantity) { }
