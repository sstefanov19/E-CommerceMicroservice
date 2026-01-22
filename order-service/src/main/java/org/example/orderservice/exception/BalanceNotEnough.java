package org.example.orderservice.exception;

public class BalanceNotEnough extends RuntimeException {
  public BalanceNotEnough(String message) {
    super(message);
  }
}
