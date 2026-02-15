package com.enterprise.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RiskIntegrationService {

    // Simula uma chamada lenta a um serviço externo
    public void checkSpc() {
        sleep(2000, "SPC/Serasa");
    }

    public void checkReceitaFederal() {
        sleep(1000, "Receita Federal");
    }

    public void checkInternalHistory() {
        sleep(1500, "Histórico Interno");
    }

    // Método auxiliar para simular latência
    private void sleep(long millis, String serviceName) {
        try {
            log.info("⏳ Iniciando verificação: {}", serviceName);
            Thread.sleep(millis); // Na Virtual Thread, isso "pausa" sem bloquear a CPU
            log.info("✅ Finalizado: {}", serviceName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
