package com.mycompany.microservice.credit.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for updating a credit application (by Analyst/Admin).
 */
public record UpdateApplicationRequest(
        @DecimalMin(value = "0.01", message = "Interest rate must be positive") @DecimalMax(value = "100", message = "Interest rate cannot exceed 100%") BigDecimal interestRate,

        @Size(max = 1000, message = "Notes must be less than 1000 characters") String analystNotes,

        String status // PENDIENTE, EN_REVISION, APROBADA, RECHAZADA
) {
}
