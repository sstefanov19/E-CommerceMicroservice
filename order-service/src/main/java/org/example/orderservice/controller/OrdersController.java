package org.example.orderservice.controller;

import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService  = orderService;
    }


    @PostMapping
    public ResponseEntity<OrderResponse> makeOrder(@RequestBody OrderRequest request) {
        OrderResponse order = orderService.makeOrder(request);


        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

}
