package com.supermarket.payment_optimizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod {
    public String id;
    public Integer discount;
    public BigDecimal limit;
}
