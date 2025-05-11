package com.supermarket.payment_optimizer.dto;

import java.math.BigDecimal;

public class OrderPaymentOption {
    private BigDecimal finalCost;
    private BigDecimal pointsUsed;
    private BigDecimal cardUsed;
    private String cardId;
    private int discount;
}

