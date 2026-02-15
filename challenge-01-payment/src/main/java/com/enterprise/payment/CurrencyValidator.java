package com.enterprise.payment;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CurrencyValidator implements PaymentValidator {
    
    private static final List<String> ALLOWED_CURRENCIES = List.of("USD", "BRL", "EUR");

    @Override
    public void validate(PaymentRequest request) {
        if (!ALLOWED_CURRENCIES.contains(request.currency())) {
            throw new IllegalArgumentException("Erro: Moeda não suportada. Use apenas: " + ALLOWED_CURRENCIES);
        }
    }
}
