package com.enterprise.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    // O Spring injeta AmountValidator e CurrencyValidator aqui automaticamente!
    private final List<PaymentValidator> validators;

    @SneakyThrows
    public String processPayment(PaymentRequest request) {
        // 1. Aplica todas as regras de validação (Strategy Pattern)
        validators.forEach(validator -> validator.validate(request));

        // 2. Se passou, prepara o envio
        String paymentId = UUID.randomUUID().toString();
        String jsonMessage = objectMapper.writeValueAsString(request);

        // 3. Envia para o Kafka
        kafkaTemplate.send("payment-created-topic", paymentId, jsonMessage);

        return paymentId;
    }
}
