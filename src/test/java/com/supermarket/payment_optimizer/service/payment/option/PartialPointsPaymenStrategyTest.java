package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

class PartialPointsPaymentStrategyTest {

    private final PartialPointsPaymentStrategy strategy = new PartialPointsPaymentStrategy();

    @Test
    void shouldReturnNull_WhenPointsBelowThreshold() {
        Order order = new Order();
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");
        points.setLimit(new BigDecimal("5.00"));
        points.setDiscount(10);

        PaymentMethod card = new PaymentMethod();
        card.setId("CARD1");
        card.setLimit(new BigDecimal("100.00"));

        Map<String, PaymentMethod> methods = Map.of(
                "PUNKTY", points,
                "CARD1", card
        );

        OrderPaymentOption result = strategy.evaluate(order, methods);

        assertNull(result);
    }

    @Test
    void shouldReturnNull_WhenNoFallbackCard() {
        Order order = new Order();
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");
        points.setLimit(new BigDecimal("20.00"));
        points.setDiscount(10);

        Map<String, PaymentMethod> methods = Map.of("PUNKTY", points);

        OrderPaymentOption result = strategy.evaluate(order, methods);

        assertNull(result);
    }
}

