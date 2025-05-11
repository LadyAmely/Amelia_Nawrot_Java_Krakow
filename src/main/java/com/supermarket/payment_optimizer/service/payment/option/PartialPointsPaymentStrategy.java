package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PartialPointsPaymentStrategy implements PaymentStrategy {

    private static final String POINTS_ID = "PUNKTY";

    @Override
    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        BigDecimal value = order.getValue();
        PaymentMethod points = methodMap.get(POINTS_ID);
        if (points == null) return null;

        BigDecimal tenPercent = value.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        if (points.getLimit().compareTo(tenPercent) < 0) return null;

        BigDecimal discount = BigDecimal.valueOf(0.10);
        BigDecimal finalCost = value.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxPoints = points.getLimit().min(finalCost);
        BigDecimal remaining = finalCost.subtract(maxPoints);

        String fallbackCardId = findCardWithEnoughLimit(methodMap, remaining);
        if (fallbackCardId == null) return null;

        return new OrderPaymentOption(finalCost, maxPoints, remaining, fallbackCardId);
    }

    private String findCardWithEnoughLimit(Map<String, PaymentMethod> methods, BigDecimal requiredAmount) {
        for (PaymentMethod method : methods.values()) {
            if (!method.getId().equals(POINTS_ID) && method.getLimit().compareTo(requiredAmount) >= 0) {
                return method.getId();
            }
        }
        return null;
    }
}

