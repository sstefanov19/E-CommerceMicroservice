package org.example.orderservice.service;


import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.dto.ProductResponse;
import org.example.orderservice.entity.OrderEnum;
import org.example.orderservice.exception.QuantityNotEnough;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final ProductService productService;

    public OrderService(ProductService productService) {
        this.productService = productService;
    }


    // TODO best way to handle orders think of a transaction so
    // we know the user funds has been properly dealt with and we got money after their transaction ended
    //TODO find the best payment provider for the current use case

    public OrderResponse makeOrder(OrderRequest request) {
        // check if user has balance.
        // check if item has quantity of more than the requested amount or equal ✔️
        // process transaction
        // update status

        ProductResponse product = productService.getProductByName(request.getItem_name());

        if(product.getQuantity() < request.getQuantity()) {
            throw new QuantityNotEnough("We dont have enough quantity for the item");
        }

        BigDecimal totalPrice = new BigDecimal(request.getQuantity()).multiply(product.getPrice());

        OrderResponse newOrder = OrderResponse.builder()
                .item_name(request.getItem_name())
                .category(request.getCategory())
                .totalPrice(totalPrice)
                .status(OrderEnum.PENDING)
                .build();


        return newOrder;
    }


}
