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
        String userEmail,
        String userName,
        BigDecimal amount,
        Integer termMonths,
        String purpose,
        BigDecimal interestRate,
        String status,
        String statusDescription,
        String analystNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static ApplicationResponse from(CreditApplication app) {
        return new ApplicationResponse(
                app.getId(),
                app.getUserId(),
                app.getUserEmail(),
                app.getUserName(),
                app.getAmount(),
                app.getTermMonths(),
                app.getPurpose(),
                app.getInterestRate(),
                app.getStatus().name(),
                app.getStatus().getDescription(),
                app.getAnalystNotes(),
                app.getCreatedAt(),
                app.getUpdatedAt());
    }
}
