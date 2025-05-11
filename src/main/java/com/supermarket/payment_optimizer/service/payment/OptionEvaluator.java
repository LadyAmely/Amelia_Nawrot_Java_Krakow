package com.supermarket.payment_optimizer.service.payment;
import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class OptionEvaluator {

    private static final String POINTS_ID = "PUNKTY";

    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        BigDecimal orderValue = order.getValue();
        BigDecimal bestFinalCost = null;
        BigDecimal bestPointsUsed = BigDecimal.ZERO;
        BigDecimal bestCardUsed = BigDecimal.ZERO;
        String bestCardId = null;

        PaymentMethod pointsMethod = methodMap.get(POINTS_ID);

        if (pointsMethod != null && pointsMethod.getLimit().compareTo(orderValue) >= 0) {
            BigDecimal discount = BigDecimal.valueOf(pointsMethod.getDiscount()).divide(BigDecimal.valueOf(100));
            BigDecimal finalCost = orderValue.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
            bestFinalCost = finalCost;
            bestPointsUsed = finalCost;
            bestCardUsed = BigDecimal.ZERO;
            bestCardId = null;
        }

        BigDecimal tenPercent = orderValue.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        if (pointsMethod != null && pointsMethod.getLimit().compareTo(tenPercent) >= 0) {
            BigDecimal discount = BigDecimal.valueOf(0.10);
            BigDecimal finalCost = orderValue.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal maxPoints = pointsMethod.getLimit().min(finalCost);
            BigDecimal remaining = finalCost.subtract(maxPoints);
            String cardId = findCardWithEnoughLimit(methodMap, remaining);

            if (cardId != null && isBetter(finalCost, bestFinalCost, maxPoints, bestPointsUsed)) {
                bestFinalCost = finalCost;
                bestPointsUsed = maxPoints;
                bestCardUsed = remaining;
                bestCardId = cardId;
            }
        }

        if (order.getPromotions() != null) {
            for (String promo : order.getPromotions()) {
                PaymentMethod method = methodMap.get(promo);
                if (method != null && method.getLimit().compareTo(orderValue) >= 0) {
                    BigDecimal discount = BigDecimal.valueOf(method.getDiscount()).divide(BigDecimal.valueOf(100));
                    BigDecimal finalCost = orderValue.multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);

                    if (isBetter(finalCost, bestFinalCost, BigDecimal.ZERO, bestPointsUsed)) {
                        bestFinalCost = finalCost;
                        bestPointsUsed = BigDecimal.ZERO;
                        bestCardUsed = finalCost;
                        bestCardId = method.getId();
                    }
                }
            }
        }

        for (PaymentMethod method : methodMap.values()) {
            if (!method.getId().equals(POINTS_ID) && method.getLimit().compareTo(orderValue) >= 0) {
                BigDecimal finalCost = orderValue.setScale(2, RoundingMode.HALF_UP);
                if (isBetter(finalCost, bestFinalCost, BigDecimal.ZERO, bestPointsUsed)) {
                    bestFinalCost = finalCost;
                    bestPointsUsed = BigDecimal.ZERO;
                    bestCardUsed = finalCost;
                    bestCardId = method.getId();
                }
            }
        }

        return new OrderPaymentOption(bestFinalCost, bestPointsUsed, bestCardUsed, bestCardId);
    }

    private boolean isBetter(BigDecimal candidateCost, BigDecimal currentBest, BigDecimal candidatePoints, BigDecimal currentPoints) {
        if (currentBest == null) return true;
        int cmp = candidateCost.compareTo(currentBest);
        return cmp < 0 || (cmp == 0 && candidatePoints.compareTo(currentPoints) > 0);
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

