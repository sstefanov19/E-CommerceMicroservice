package org.example.orderservice.exception;

public class QuantityNotEnough extends RuntimeException {
    public QuantityNotEnough(String message) {
        super(message);
    }
}
