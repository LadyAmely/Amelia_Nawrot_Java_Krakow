package com.supermarket.payment_optimizer.service.payment.factory;

import com.supermarket.payment_optimizer.service.payment.option.*;

import java.util.List;

public class PaymentStrategyFactory {
    public List<PaymentStrategy> getStrategies() {
        return List.of(
                new FullPointsPaymentStrategy(),
                new PartialPointsPaymentStrategy(),
                new PromoCardPaymentStrategy(),
                new FallbackCardPaymentStrategy()
        );
    }
}

