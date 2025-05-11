package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

class PromoCardPaymentStrategyTest {

    private final PromoCardPaymentStrategy strategy = new PromoCardPaymentStrategy();

    @Test
    void shouldReturnNull_WhenNoPromotions() {
        Order order = new Order();
        order.setValue(new BigDecimal("120.00"));
        order.setPromotions(null);

        PaymentMethod promo = new PaymentMethod();
        promo.setId("PROMO1");
        promo.setLimit(new BigDecimal("200.00"));
        promo.setDiscount(20);

        Map<String, PaymentMethod> methods = Map.of("PROMO1", promo);

        OrderPaymentOption result = strategy.evaluate(order, methods);

        assertNull(result);
    }

    @Test
    void shouldReturnNull_WhenPromoCardLimitTooLow() {
        Order order = new Order();
        order.setValue(new BigDecimal("150.00"));
        order.setPromotions(List.of("PROMO1"));

        PaymentMethod promo = new PaymentMethod();
        promo.setId("PROMO1");
        promo.setLimit(new BigDecimal("100.00")); // < 150
        promo.setDiscount(20);

        Map<String, PaymentMethod> methods = Map.of("PROMO1", promo);

        OrderPaymentOption result = strategy.evaluate(order, methods);

        assertNull(result);
    }
}

