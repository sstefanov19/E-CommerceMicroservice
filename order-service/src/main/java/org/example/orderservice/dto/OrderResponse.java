package org.example.orderservice.dto;

import lombok.Builder;
import org.example.orderservice.entity.OrderEnum;

import java.math.BigDecimal;

@Builder
public class OrderResponse {
    private Long id;

    private Long user_id;

    private String category;

    private String item_name;

    private Integer quantity;

    private BigDecimal totalPrice;

    private OrderEnum status;

}
