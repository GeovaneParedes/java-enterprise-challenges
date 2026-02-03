package com.enterprise.notification;

import java.math.BigDecimal;

// Record: Java cria construtor, getters e toString automaticamente!
public record PaymentPayload(BigDecimal amount, String currency) {}
