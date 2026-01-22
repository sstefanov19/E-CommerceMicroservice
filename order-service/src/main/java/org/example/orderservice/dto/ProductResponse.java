package org.example.orderservice.dto;
import java.math.BigDecimal;

public record ProductResponse (

     String category,
     String name,
     BigDecimal price,
     Integer quantity
){}
