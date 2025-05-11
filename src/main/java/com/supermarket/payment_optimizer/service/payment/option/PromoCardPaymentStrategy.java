package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PromoCardPaymentStrategy implements PaymentStrategy {

    @Override
    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        if (order.getPromotions() == null) return null;

        BigDecimal value = order.getValue();

        for (String promo : order.getPromotions()) {
            PaymentMethod method = methodMap.get(promo);
            if (method != null && method.getLimit().compareTo(value) >= 0) {
                BigDecimal discount = BigDecimal.valueOf(method.getDiscount()).divide(BigDecimal.valueOf(100));
                BigDecimal finalCost = value.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
                return new OrderPaymentOption(finalCost, BigDecimal.ZERO, finalCost, method.getId());
            }
        }

        return null;
    }
}

