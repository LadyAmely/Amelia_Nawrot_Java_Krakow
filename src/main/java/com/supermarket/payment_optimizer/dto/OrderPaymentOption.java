package com.supermarket.payment_optimizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderPaymentOption {
    private BigDecimal finalCost;
    private BigDecimal pointsUsed;
    private BigDecimal cardUsed;
    private String cardId;
}

