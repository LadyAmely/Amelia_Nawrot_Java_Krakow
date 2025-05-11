
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
        if (order.getPromotions() == null || order.getPromotions().isEmpty()) return null;

        BigDecimal value = order.getValue();

        return order.getPromotions().stream()
                .map(methodMap::get)
                .filter(pm -> pm != null && pm.getLimit().compareTo(value) >= 0)
                .findFirst()
                .map(pm -> {
                    BigDecimal discount = BigDecimal.valueOf(pm.getDiscount()).divide(BigDecimal.valueOf(100));
                    BigDecimal finalCost = value.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
                    return new OrderPaymentOption(finalCost, BigDecimal.ZERO, finalCost, pm.getId());
                })
                .orElse(null);
    }
}


