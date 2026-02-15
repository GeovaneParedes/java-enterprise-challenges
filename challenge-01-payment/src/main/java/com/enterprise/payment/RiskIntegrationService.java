package com.enterprise.payment;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
public class RiskIntegrationService {

    // Instância configurada no application.yml
    @CircuitBreaker(name = "risk-service", fallbackMethod = "fallbackRisk")
    public void checkSpc() {
        // SIMULAÇÃO DE ERRO: 50% de chance de falhar
        if (new Random().nextBoolean()) {
            log.error("❌ ERRO: O sistema do SPC caiu!");
            throw new RuntimeException("SPC Indisponível");
        }
        sleep(500, "SPC/Serasa"); // Reduzi para 500ms para testarmos mais rápido
    }

    public void checkReceitaFederal() {
        sleep(200, "Receita Federal");
    }

    public void checkInternalHistory() {
        sleep(100, "Histórico Interno");
    }

    // MÉTODO DE FALLBACK
    // Deve ter a mesma assinatura do original + Exception
    public void fallbackRisk(Throwable t) {
        log.warn("🛡️ CIRCUIT BREAKER ATIVADO: Ignorando SPC (Sistema fora do ar). Motivo: {}", t.getMessage());
        // Aqui poderíamos lançar erro ou aprovar com ressalvas. 
        // Vamos apenas logar e deixar passar (Aprovação de Contingência).
    }

    private void sleep(long millis, String serviceName) {
        try {
            log.info("⏳ Verificando: {}", serviceName);
            Thread.sleep(millis);
            log.info("✅ OK: {}", serviceName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
