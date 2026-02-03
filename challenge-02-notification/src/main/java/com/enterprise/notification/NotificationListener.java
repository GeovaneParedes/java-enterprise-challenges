package com.enterprise.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor // Cria construtor automático para injetar o Repository
public class NotificationListener {

    private final NotificationRepository repository;

    @KafkaListener(topics = "payment-created-topic", groupId = "notification-group")
    public void consume(String message) {
        log.info("📧 KAFKA: Mensagem recebida: {}", message);
        
        // Passo 1: Criar o objeto
        Notification notification = new Notification(message);
        
        // Passo 2: Salvar no banco H2
        repository.save(notification);
        
        log.info("💾 PERSISTÊNCIA: Notificação salva no banco com ID: {}", notification.getId());
    }
}
