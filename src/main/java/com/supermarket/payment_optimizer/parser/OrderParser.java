package com.supermarket.payment_optimizer.parser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.payment_optimizer.model.Order;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class OrderParser {

    public static List<Order> parseOrders(String classpathLocation) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String cleanedPath = "/" + classpathLocation.replace("classpath:", "").replace("\\", "/");

        InputStream is = OrderParser.class.getResourceAsStream(cleanedPath);

        if (is == null) {
            throw new IOException("Nie znaleziono pliku: " + classpathLocation);
        }

        return Arrays.asList(mapper.readValue(is, Order[].class));
    }
}
