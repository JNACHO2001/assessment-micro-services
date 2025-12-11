package com.mycompany.microservice.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for user registration request.
 */
public record RegisterRequest(
        @NotBlank(message = "Document is required") String document,

        @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Password is required") @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters") String password,

        @NotNull(message = "Salary is required") String salary) {
}
