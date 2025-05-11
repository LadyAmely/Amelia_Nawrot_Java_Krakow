package com.supermarket.payment_optimizer.controller;

import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.model.PaymentMethod;
import com.supermarket.payment_optimizer.parser.OrderParser;
import com.supermarket.payment_optimizer.parser.PaymentMethodParser;
import com.supermarket.payment_optimizer.service.PaymentOptimizerService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentOptimizerService optimizerService;

    public PaymentController(PaymentOptimizerService optimizerService) {
        this.optimizerService = optimizerService;
    }

    @GetMapping("/optimize")
    public Map<String, BigDecimal> optimize() throws IOException {
        List<Order> orders = OrderParser.parseOrders("data/orders.json");
        List<PaymentMethod> methods = PaymentMethodParser.parsePaymentMethods("data/paymentmethods.json");

        return optimizerService.optimize(orders, methods);
    }
}


