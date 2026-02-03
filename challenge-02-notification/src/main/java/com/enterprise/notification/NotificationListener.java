package com.enterprise.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationListener {

    @KafkaListener(topics = "payment-created-topic", groupId = "notification-group")
    public void consume(String message) {
        log.info("📧 NOTIFICAÇÃO: Recebi um evento de pagamento!");
        log.info("Conteúdo: {}", message);
        log.info("Enviando e-mail simulado para o cliente... [ENVIADO]");
    }
}
