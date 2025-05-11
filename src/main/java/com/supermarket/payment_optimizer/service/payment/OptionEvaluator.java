package com.supermarket.payment_optimizer.service.payment;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import com.supermarket.payment_optimizer.service.payment.option.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OptionEvaluator {

    private final List<PaymentStrategy> strategies = Arrays.asList(
            new FullPointsPaymentStrategy(),
            new PartialPointsPaymentStrategy(),
            new PromoCardPaymentStrategy(),
            new FallbackCardPaymentStrategy()
    );

    public OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap) {
        OrderPaymentOption best = null;

        for (PaymentStrategy strategy : strategies) {
            OrderPaymentOption candidate = strategy.evaluate(order, methodMap);
            if (candidate != null && isBetter(candidate, best)) {
                best = candidate;
            }
        }

        return best;
    }

    private boolean isBetter(OrderPaymentOption candidate, OrderPaymentOption currentBest) {
        if (currentBest == null) return true;
        int costComparison = candidate.getFinalCost().compareTo(currentBest.getFinalCost());
        if (costComparison < 0) return true;
        if (costComparison == 0) {
            return candidate.getPointsUsed().compareTo(currentBest.getPointsUsed()) > 0;
        }
        return false;
    }
}
