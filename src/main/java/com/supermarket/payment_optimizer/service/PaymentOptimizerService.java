package com.supermarket.payment_optimizer.service;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import com.supermarket.payment_optimizer.service.payment.OptionEvaluator;
import com.supermarket.payment_optimizer.service.payment.PaymentApplier;
import com.supermarket.payment_optimizer.service.payment.factory.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentOptimizerService {

    private final PaymentApplier applier = new PaymentApplier();
    PaymentStrategyFactory factory = new PaymentStrategyFactory();
    OptionEvaluator evaluator = new OptionEvaluator(factory);


    public Map<String, BigDecimal> optimize(List<Order> orders, List<PaymentMethod> methods) {
        Map<String, BigDecimal> usedAmounts = new HashMap<>();
        Map<String, PaymentMethod> methodMap = new HashMap<>();
        for (PaymentMethod method : methods) {
            methodMap.put(method.getId(), new PaymentMethod(
                    method.getId(),
                    method.getDiscount(),
                    method.getLimit()
            ));
        }

        for (Order order : orders) {
            OrderPaymentOption option = evaluator.evaluate(order, methodMap);
            applier.applyPayment(
                    option.getPointsUsed(),
                    option.getCardUsed(),
                    option.getCardId(),
                    methodMap,
                    usedAmounts
            );
        }

        usedAmounts.replaceAll((k, v) -> v.setScale(2, RoundingMode.HALF_UP));
        return usedAmounts;
    }
}