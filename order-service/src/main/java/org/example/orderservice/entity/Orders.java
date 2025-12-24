package org.example.orderservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name= "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;

    private Long user_id;

    private String category;

    private String item_name;

    private Integer quantity;

    private BigDecimal totalPrice;

}
