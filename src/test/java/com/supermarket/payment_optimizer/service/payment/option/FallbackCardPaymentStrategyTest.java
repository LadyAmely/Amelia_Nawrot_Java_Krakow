package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FallbackCardPaymentStrategyTest {

    private final FallbackCardPaymentStrategy strategy = new FallbackCardPaymentStrategy();

    @Test
    void shouldReturnNull_WhenNoCardsHaveEnoughLimit() {
        Order order = new Order();
        order.setValue(new BigDecimal("200.00"));

        PaymentMethod card1 = new PaymentMethod();
        card1.setId("VISA");
        card1.setLimit(new BigDecimal("150.00"));

        PaymentMethod card2 = new PaymentMethod();
        card2.setId("MASTERCARD");
        card2.setLimit(new BigDecimal("100.00"));

        Map<String, PaymentMethod> methodMap = Map.of(
                "VISA", card1,
                "MASTERCARD", card2
        );

        OrderPaymentOption result = strategy.evaluate(order, methodMap);

        assertNull(result, "Should return null if no card can cover the full order amount");
    }

    @Test
    void shouldReturnNull_WhenOnlyPointsAreAvailable() {
        Order order = new Order();
        order.setValue(new BigDecimal("100.00"));

        PaymentMethod points = new PaymentMethod();
        points.setId("PUNKTY");
        points.setLimit(new BigDecimal("500.00"));

        Map<String, PaymentMethod> methodMap = Map.of("PUNKTY", points);

        OrderPaymentOption result = strategy.evaluate(order, methodMap);

        assertNull(result, "Should return null if only 'PUNKTY' method is available");
    }
}

