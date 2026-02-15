package com.enterprise.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        String paymentId = paymentService.processPayment(request);
        return ResponseEntity.ok("Pagamento " + paymentId + " processado com sucesso!");
    }

    // Tratamento de Erro Simplificado (ExceptionHandler local)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationErrors(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
