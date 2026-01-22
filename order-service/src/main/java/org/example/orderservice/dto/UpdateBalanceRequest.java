package org.example.orderservice.dto;

import java.math.BigDecimal;

public record UpdateBalanceRequest (BigDecimal cost) {}
