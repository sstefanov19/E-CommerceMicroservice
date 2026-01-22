package org.example.orderservice.service;


import jakarta.transaction.Transactional;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.dto.ProductResponse;
import org.example.orderservice.entity.OrderEnum;
import org.example.orderservice.entity.Orders;
import org.example.orderservice.event.OrderCreatedEvent;
import org.example.orderservice.event.OrderEventProducer;
import org.example.orderservice.exception.BalanceNotEnough;
import org.example.orderservice.exception.QuantityNotEnough;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(ProductService productService, UserService userService, OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.productService = productService;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public OrderResponse makeOrder(OrderRequest request) {
        // 1. Synchronous REST call to validate product availability
        ProductResponse product = productService.getProductByName(request.item_name());

        if (product.getQuantity() < request.quantity()) {
            throw new QuantityNotEnough("We dont have enough quantity for the item");
        }

        BigDecimal totalPrice = new BigDecimal(request.quantity()).multiply(product.getPrice());

        // 2. Synchronous REST call to get userId
        Long userId = userService.getUserId();

        // 3. Synchronous REST call to validate balance (read-only check)
        BigDecimal userBalance = userService.getUserBalance();
        if (userBalance.compareTo(totalPrice) < 0) {
            throw new BalanceNotEnough("User balance is not enough make sure you have enough balance");
        }

        // 4. Create and save order
        Orders dbOrder = Orders.builder()
                .itemName(request.item_name())
                .userId(userId)
                .category(request.category())
                .totalPrice(totalPrice)
                .status(OrderEnum.PENDING)
                .quantity(request.quantity())
                .createdDate(LocalDateTime.now())
                .completedDate(LocalDateTime.now().plusDays(7))
                .build();

        orderRepository.save(dbOrder);

        // 5. Publish OrderCreatedEvent to Kafka (balance deduction + inventory reduction happens async)
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(dbOrder.getId())
                .userId(userId)
                .productName(request.item_name())
                .category(request.category())
                .quantity(request.quantity())
                .totalPrice(totalPrice)
                .createdAt(dbOrder.getCreatedDate())
                .eventId(UUID.randomUUID().toString())
                .eventType("ORDER_CREATED")
                .eventTimestamp(LocalDateTime.now())
                .build();

        orderEventProducer.publishOrderCreatedEvent(event);

        // 6. Return response
        return OrderResponse.builder()
                .id(dbOrder.getId())
                .userId(userId)
                .item_name(request.item_name())
                .category(request.category())
                .totalPrice(totalPrice)
                .status(OrderEnum.PENDING)
                .quantity(request.quantity())
                .createdDate(dbOrder.getCreatedDate())
                .completedDate(dbOrder.getCompletedDate())
                .build();
    }


        public List<OrderResponse> getAllOrders() {

            List<Orders> orders = orderRepository.findAll();

            return orders.stream()
                    .map(this::mapToResponse)
                    .toList();
        }


        private OrderResponse mapToResponse(Orders order) {

            OrderEnum effectiveStatus = order.getStatus();

            if(effectiveStatus == OrderEnum.PENDING && order.getCompletedDate() != null) {
               LocalDateTime now = LocalDateTime.now();
                if (order.getCompletedDate().isBefore(now)){
                    effectiveStatus = OrderEnum.SUCCESS;
                }
            }

            return OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .category(order.getCategory())
                    .item_name(order.getItemName())
                    .quantity(order.getQuantity())
                    .totalPrice(order.getTotalPrice())
                    .status(effectiveStatus)
                    .createdDate(order.getCreatedDate())
                    .completedDate(order.getCompletedDate())
                    .build();
        }
}
