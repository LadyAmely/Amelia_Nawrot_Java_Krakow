package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

class FullPointsPaymentStrategyTest {

    private final FullPointsPaymentStrategy strategy = new FullPointsPaymentStrategy();

    @Test
    void shouldReturnNull_WhenPointsAreInsufficient() {
        Order order = new Order();
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");
        points.setLimit(new BigDecimal("50.00"));
        points.setDiscount(10);

        Map<String, PaymentMethod> methodMap = Map.of("PUNKTY", points);

        OrderPaymentOption result = strategy.evaluate(order, methodMap);

        assertNull(result);
    }

    @Test
    void shouldReturnNull_WhenPointsNotPresent() {
        Order order = new Order();
        order.setValue(new BigDecimal("100.00"));

        Map<String, PaymentMethod> emptyMap = Map.of();

        OrderPaymentOption result = strategy.evaluate(order, emptyMap);

        assertNull(result);
    }
}
