package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class FallbackCardPaymentStrategy implements PaymentStrategy {

    private static final String POINTS_ID = "PUNKTY";

    @Override
    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        return methodMap.values().stream()
                .filter(method -> !POINTS_ID.equals(method.getId()))
                .filter(method -> method.getLimit().compareTo(order.getValue()) >= 0)
                .findFirst()
                .map(method -> {
                    BigDecimal finalCost = order.getValue().setScale(2, RoundingMode.HALF_UP);
                    return new OrderPaymentOption(finalCost, BigDecimal.ZERO, finalCost, method.getId());
                })
                .orElse(null);
    }

}

