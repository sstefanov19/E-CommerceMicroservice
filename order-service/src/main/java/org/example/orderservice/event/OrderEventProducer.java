package org.example.orderservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        String key = String.valueOf(event.getOrderId());

        log.info("Publishing OrderCreatedEvent: orderId={}, userId={}, product={}",
                event.getOrderId(), event.getUserId(), event.getProductName());

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                kafkaTemplate.send(orderEventsTopic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("OrderCreatedEvent published successfully: orderId={}, partition={}, offset={}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish OrderCreatedEvent: orderId={}, error={}",
                        event.getOrderId(), ex.getMessage(), ex);
            }
        });
    }
}