package com.supermarket.payment_optimizer.controller;

import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders() throws IOException {
        return orderService.loadOrders("data/orders.json");
    }
}

