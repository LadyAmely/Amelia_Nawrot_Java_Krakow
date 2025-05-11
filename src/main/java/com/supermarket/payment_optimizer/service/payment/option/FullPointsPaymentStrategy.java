package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;
import java.math.RoundingMode;

public class FullPointsPaymentStrategy implements PaymentStrategy {

    private static final String POINTS_ID = "PUNKTY";

    @Override
    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        BigDecimal value = order.getValue();
        PaymentMethod points = methodMap.get(POINTS_ID);
        if (points != null && points.getLimit().compareTo(value) >= 0) {
            BigDecimal discount = BigDecimal.valueOf(points.getDiscount()).divide(BigDecimal.valueOf(100));
            BigDecimal finalCost = value.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
            return new OrderPaymentOption(finalCost, finalCost, BigDecimal.ZERO, null);
        }
        return null;
    }
}

