package com.enterprise.payment;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class AmountValidator implements PaymentValidator {
    
    @Override
    public void validate(PaymentRequest request) {
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Erro: O valor do pagamento deve ser positivo.");
        }
    }
}
