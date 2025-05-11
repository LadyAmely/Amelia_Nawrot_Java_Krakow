package com.supermarket.payment_optimizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String id;
    private BigDecimal value;
    private List<String> promotions;
}
