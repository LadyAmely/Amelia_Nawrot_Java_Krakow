package com.supermarket.payment_optimizer.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.payment_optimizer.model.PaymentMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class PaymentMethodParser {

    public static List<PaymentMethod> parsePaymentMethods(String classpathLocation) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String cleanedPath = "/" + classpathLocation.replace("classpath:", "").replace("\\", "/");
        InputStream is = PaymentMethodParser.class.getResourceAsStream(cleanedPath);

        if (is == null) {
            throw new IOException("Nie znaleziono pliku: " + classpathLocation);
        }

        return Arrays.asList(mapper.readValue(is, PaymentMethod[].class));
    }
}
