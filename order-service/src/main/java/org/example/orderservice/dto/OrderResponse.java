package org.example.orderservice.dto;

import lombok.Builder;

import org.example.orderservice.entity.OrderEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse (
     Long id,
     Long userId,
     String category,
     String item_name,
     Integer quantity,
     BigDecimal totalPrice,
     OrderEnum status,
     LocalDateTime createdDate,
     LocalDateTime completedDate
){}
