package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

public class PartialPointsPaymentStrategy implements PaymentStrategy {

    private static final String POINTS_ID = "PUNKTY";
    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");

    @Override
    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        PaymentMethod points = methodMap.get(POINTS_ID);
        if (points == null) return null;

        BigDecimal orderValue = order.getValue();
        BigDecimal minRequiredPoints = orderValue.multiply(TEN_PERCENT).setScale(2, RoundingMode.HALF_UP);
        if (points.getLimit().compareTo(minRequiredPoints) < 0) return null;

        BigDecimal discountedTotal = orderValue.multiply(BigDecimal.ONE.subtract(TEN_PERCENT)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pointsUsed = points.getLimit().min(discountedTotal);
        BigDecimal remainingToPay = discountedTotal.subtract(pointsUsed);

        return findFallbackCard(methodMap, remainingToPay)
                .map(cardId -> new OrderPaymentOption(discountedTotal, pointsUsed, remainingToPay, cardId))
                .orElse(null);
    }

    private Optional<String> findFallbackCard(Map<String, PaymentMethod> methods, BigDecimal amount) {
        return methods.values().stream()
                .filter(m -> !POINTS_ID.equals(m.getId()))
                .filter(m -> m.getLimit().compareTo(amount) >= 0)
                .map(PaymentMethod::getId)
                .findFirst();
    }
}


