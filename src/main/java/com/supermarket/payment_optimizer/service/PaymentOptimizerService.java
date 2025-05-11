package com.supermarket.payment_optimizer.service;

import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentOptimizerService {

    private static final String POINTS_ID = "PUNKTY";

    public Map<String, BigDecimal> optimize(List<Order> orders, List<PaymentMethod> methods) {
        Map<String, BigDecimal> usedAmounts = new HashMap<>();
        Map<String, PaymentMethod> methodMap = new HashMap<>();
        for (PaymentMethod method : methods) {
            methodMap.put(method.getId(), new PaymentMethod(method.getId(), method.getDiscount(), method.getLimit()));
        }

        for (Order order : orders) {
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
                BigDecimal maxPoints = pointsMethod.getLimit().min(finalCost); // ile punktów możemy użyć
                BigDecimal remaining = finalCost.subtract(maxPoints);

                String cardId = findCardWithEnoughLimit(methodMap, remaining);
                if (cardId != null) {
                    if (isBetter(finalCost, bestFinalCost, maxPoints, bestPointsUsed)) {
                        bestFinalCost = finalCost;
                        bestPointsUsed = maxPoints;
                        bestCardUsed = remaining;
                        bestCardId = cardId;
                    }
                }
            }

            if (order.getPromotions() != null) {
                for (String promo : order.getPromotions()) {
                    PaymentMethod method = methodMap.get(promo);
                    if (method == null) continue;
                    if (method.getLimit().compareTo(orderValue) >= 0) {
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

            for (PaymentMethod method : methods) {
                if (method.getId().equals(POINTS_ID)) continue;
                if (method.getLimit().compareTo(orderValue) >= 0) {
                    BigDecimal finalCost = orderValue.setScale(2, RoundingMode.HALF_UP);
                    if (isBetter(finalCost, bestFinalCost, BigDecimal.ZERO, bestPointsUsed)) {
                        bestFinalCost = finalCost;
                        bestPointsUsed = BigDecimal.ZERO;
                        bestCardUsed = finalCost;
                        bestCardId = method.getId();
                    }
                }
            }

            if (bestPointsUsed.compareTo(BigDecimal.ZERO) > 0) {
                PaymentMethod method = methodMap.get(POINTS_ID);
                method.setLimit(method.getLimit().subtract(bestPointsUsed));
                usedAmounts.merge(POINTS_ID, bestPointsUsed, BigDecimal::add);
            }
            if (bestCardUsed.compareTo(BigDecimal.ZERO) > 0 && bestCardId != null) {
                PaymentMethod method = methodMap.get(bestCardId);
                method.setLimit(method.getLimit().subtract(bestCardUsed));
                usedAmounts.merge(bestCardId, bestCardUsed, BigDecimal::add);
            }
        }

        usedAmounts.replaceAll((k, v) -> v.setScale(2, RoundingMode.HALF_UP));
        return usedAmounts;
    }

    private boolean isBetter(BigDecimal candidateCost, BigDecimal currentBest, BigDecimal candidatePoints, BigDecimal currentPoints) {
        if (currentBest == null) return true;
        int cmp = candidateCost.compareTo(currentBest);
        if (cmp < 0) return true;
        if (cmp == 0) return candidatePoints.compareTo(currentPoints) > 0;
        return false;
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
