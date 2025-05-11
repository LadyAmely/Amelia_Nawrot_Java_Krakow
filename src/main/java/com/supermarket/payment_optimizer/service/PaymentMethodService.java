package com.supermarket.payment_optimizer.service;

import com.supermarket.payment_optimizer.model.PaymentMethod;
import com.supermarket.payment_optimizer.parser.PaymentMethodParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PaymentMethodService {

    public List<PaymentMethod> loadPaymentMethods(String jsonFilePath) throws IOException {
        return PaymentMethodParser.parsePaymentMethods(jsonFilePath);
    }
}
