package org.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {

    String category;
    String name;
    BigDecimal price;
    Integer quantity;
}
