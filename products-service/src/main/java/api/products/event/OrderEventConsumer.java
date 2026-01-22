package api.products.event;

import api.products.entity.Product;
import api.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(
            topics = "${kafka.topic.order-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: orderId={}, product={}, quantity={}",
                event.getOrderId(), event.getProductName(), event.getQuantity());

        try {
            // Find product by name
            Product product = productRepository.getProductByName(event.getProductName());

            if (product == null) {
                log.error("Product not found: {}", event.getProductName());
                throw new RuntimeException("Product not found: " + event.getProductName());
            }

            // Reduce stock
            Integer newQuantity = product.getQuantity() - event.getQuantity();

            if (newQuantity < 0) {
                log.warn("Stock would go negative for product: {}, current={}, requested={}",
                        event.getProductName(), product.getQuantity(), event.getQuantity());
                newQuantity = 0;
            }

            product.setQuantity(newQuantity);
            productRepository.save(product);

            log.info("Stock reduced successfully: product={}, newQuantity={}, orderId={}",
                    event.getProductName(), newQuantity, event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvent: orderId={}, product={}, error={}",
                    event.getOrderId(), event.getProductName(), e.getMessage(), e);
            throw e;
        }
    }
}