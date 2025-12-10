package com.mycompany.microservice.risk;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class RiskEvaluationController {

    @PostMapping("/risk-evaluation")
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(@RequestBody RiskEvaluationRequest request) {
        String documento = request.documento();
        
        // 1. Convert document to seed (hash mod 1000)
        long seed = Math.abs(documento.hashCode()) % 1000;
        
        // 2. Generate score (300-950) based on seed
        // We map 0-999 to 300-950
        // Range size: 950 - 300 = 650
        // Score = 300 + (seed * 650 / 1000)
        int score = 300 + (int) (seed * 650 / 1000);
        
        // 3. Classify
        String nivelRiesgo;
        String detalle;
        
        if (score <= 500) {
            nivelRiesgo = "ALTO";
            detalle = "Historial crediticio deficiente o insuficiente.";
        } else if (score <= 700) {
            nivelRiesgo = "MEDIO";
            detalle = "Historial crediticio moderado.";
        } else {
            nivelRiesgo = "BAJO";
            detalle = "Excelente historial crediticio.";
        }
        
        RiskEvaluationResponse response = new RiskEvaluationResponse(
                documento,
                score,
                nivelRiesgo,
                detalle
        );
        
        return ResponseEntity.ok(response);
    }
}

record RiskEvaluationRequest(String documento, BigDecimal monto, Integer plazo) {}

record RiskEvaluationResponse(String documento, Integer score, String nivelRiesgo, String detalle) {}
