package com.enterprise.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final ObjectMapper objectMapper; // Já vem configurado no Spring Boot

    public NotificationStats calculateStats() {
        // 1. Busca tudo do banco (Em prod real usaríamos paginação, mas para estudo ok)
        List<Notification> allNotifications = repository.findAll();

        // 2. Stream Pipeline
        Map<String, Long> countByCurrency = allNotifications.stream()
            // Passo A: Transforma Entidade (String JSON) -> Record Java
            .map(n -> parseJson(n.getMessage()))
            // Passo B: Remove nulos (caso falhe o parse)
            .filter(p -> p != null)
            // Passo C: Agrupa por Moeda e Conta
            .collect(Collectors.groupingBy(
                PaymentPayload::currency, 
                Collectors.counting()
            ));

        // 3. Retorna o Record de Estatística
        return new NotificationStats(allNotifications.size(), countByCurrency);
    }

    // Helper simples para lidar com a Checked Exception do Jackson dentro do Stream
    private PaymentPayload parseJson(String json) {
        try {
            return objectMapper.readValue(json, PaymentPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Erro ao ler JSON: {}", json);
            return null;
        }
    }
}
