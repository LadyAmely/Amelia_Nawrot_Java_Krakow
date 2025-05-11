package com.supermarket.payment_optimizer.service.payment;

import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentApplier {

    private static final String POINTS_ID = "PUNKTY";

    public void applyPayment(
            BigDecimal bestPointsUsed,
            BigDecimal bestCardUsed,
            String bestCardId,
            Map<String, PaymentMethod> methodMap,
            Map<String, BigDecimal> usedAmounts
    ) {
        if (bestPointsUsed.compareTo(BigDecimal.ZERO) > 0) {
            PaymentMethod points = methodMap.get(POINTS_ID);
            points.setLimit(points.getLimit().subtract(bestPointsUsed));
            usedAmounts.merge(POINTS_ID, bestPointsUsed, BigDecimal::add);
        }

        if (bestCardUsed.compareTo(BigDecimal.ZERO) > 0 && bestCardId != null) {
            PaymentMethod card = methodMap.get(bestCardId);
            card.setLimit(card.getLimit().subtract(bestCardUsed));
            usedAmounts.merge(bestCardId, bestCardUsed, BigDecimal::add);
        }
    }
}

