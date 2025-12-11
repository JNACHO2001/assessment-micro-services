package com.mycompany.microservice.credit.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating a credit application.
 */
public record CreateApplicationRequest(
        @NotNull(message = "Amount is required") @DecimalMin(value = "100000", message = "Minimum amount is 100,000") @DecimalMax(value = "500000000", message = "Maximum amount is 500,000,000") BigDecimal amount,

        @NotNull(message = "Term is required") @Min(value = 6, message = "Minimum term is 6 months") @Max(value = 120, message = "Maximum term is 120 months") Integer termMonths,

        @NotBlank(message = "Purpose is required") @Size(max = 500, message = "Purpose must be less than 500 characters") String purpose) {
}
