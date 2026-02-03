package com.enterprise.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public String createPayment(@RequestBody String payload) {
        String transactionId = UUID.randomUUID().toString();
        log.info("Recebido pagamento. Enviando para Kafka: {}", transactionId);
        
        // Envia para o Kafka (Tópico: payment-created-topic)
        kafkaTemplate.send("payment-created-topic", transactionId, payload);
        
        return "Pagamento " + transactionId + " processado e enviado!";
    }
}
