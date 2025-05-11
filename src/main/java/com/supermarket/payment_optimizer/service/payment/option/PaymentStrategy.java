package com.supermarket.payment_optimizer.service.payment.option;

import com.supermarket.payment_optimizer.dto.OrderPaymentOption;
import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.util.Map;

public interface PaymentStrategy {
    OrderPaymentOption evaluate(Order order, Map<String, PaymentMethod> methodMap);
}
