package com.enterprise.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final List<PaymentValidator> validators;
    private final RiskIntegrationService riskService; // Injetamos o serviço lento

    @SneakyThrows
    public String processPayment(PaymentRequest request) {
        long startTime = System.currentTimeMillis();

        // 1. Validação de Regras (Strategy - Rápido)
        validators.forEach(v -> v.validate(request));

        // 2. Análise de Risco em Paralelo (Virtual Threads - Lento mas Paralelo)
        // O try-with-resources garante que o Executor fecha no final
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Disparamos as 3 tarefas simultaneamente
            Future<?> futureSpc = executor.submit(() -> riskService.checkSpc());
            Future<?> futureReceita = executor.submit(() -> riskService.checkReceitaFederal());
            Future<?> futureHistory = executor.submit(() -> riskService.checkInternalHistory());

            // .get() bloqueia a execução desta linha até terminar. 
            // Em Threads normais, isso seria ruim. Em Virtual Threads, é levíssimo!
            futureSpc.get();
            futureReceita.get();
            futureHistory.get();
        }
        
        long endTime = System.currentTimeMillis();
        log.info("⚡ Análise de Risco concluída em {} ms", (endTime - startTime));

        // 3. Envia para o Kafka
        String paymentId = UUID.randomUUID().toString();
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaTemplate.send("payment-created-topic", paymentId, jsonMessage);

        return paymentId;
    }
}
