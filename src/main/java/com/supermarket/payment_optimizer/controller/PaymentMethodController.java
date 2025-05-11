package com.supermarket.payment_optimizer.controller;

import com.supermarket.payment_optimizer.model.PaymentMethod;
import com.supermarket.payment_optimizer.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService){
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public List<PaymentMethod> getPaymentsMethods() throws IOException {
        return paymentMethodService.loadPaymentMethods("data/paymentmethods.json");
    }
}
