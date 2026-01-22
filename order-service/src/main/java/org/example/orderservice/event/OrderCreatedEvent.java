package org.example.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private String productName;
    private String category;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    private String eventId;
    private String eventType;
    private LocalDateTime eventTimestamp;
}