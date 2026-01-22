package org.example.userservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.models.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = "${kafka.topic.order-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: orderId={}, userId={}, totalPrice={}",
                event.getOrderId(), event.getUserId(), event.getTotalPrice());

        try {
            // Find user by ID (not JWT context - using userId from event)
            User user = userRepository.findById(event.getUserId())
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with ID: " + event.getUserId()));

            // Deduct balance
            user.setBalance(user.getBalance().subtract(event.getTotalPrice()));
            userRepository.save(user);

            log.info("Balance deducted successfully: userId={}, newBalance={}, orderId={}",
                    event.getUserId(), user.getBalance(), event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvent: orderId={}, userId={}, error={}",
                    event.getOrderId(), event.getUserId(), e.getMessage(), e);
            throw e;
        }
    }
}