package com.mycompany.microservice.credit.application.dto;

import com.mycompany.microservice.credit.domain.model.CreditApplication;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for credit application response.
 */
public record ApplicationResponse(
        Long id,
        Long userId,
        BigDecimal amount,
        Integer termMonths,
        String purpose,
        String status,
        String statusDescription,
        String analystNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static ApplicationResponse from(CreditApplication app) {
        return new ApplicationResponse(
                app.getId(),
                app.getUserId(),
                app.getAmount(),
                app.getTermMonths(),
                app.getPurpose(),
                app.getStatus().name(),
                app.getStatus().getDescription(),
                app.getAnalystNotes(),
                app.getCreatedAt(),
                app.getUpdatedAt());
    }
}
