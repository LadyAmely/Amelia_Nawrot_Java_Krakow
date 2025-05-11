package com.supermarket.payment_optimizer.service;

import com.supermarket.payment_optimizer.model.Order;
import com.supermarket.payment_optimizer.parser.OrderParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OrderService {

    public List<Order> loadOrders(String jsonFilePath) throws IOException {
        return OrderParser.parseOrders(jsonFilePath);
    }
}
