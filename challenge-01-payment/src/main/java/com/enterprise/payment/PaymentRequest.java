package com.enterprise.payment;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount, String currency) {}
