package com.mycompany.microservice.auth.application.dto;

/**
 * DTO for authentication response containing JWT token.
 */
public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String name,
        String role,
        Long userId,
        String message) {
    public static AuthResponse success(String token, String email, String name, String role, Long userId) {
        return new AuthResponse(token, "Bearer", email, name, role, userId, "Authentication successful");
    }

    public static AuthResponse registered(String token, String email, String name, String role, Long userId) {
        return new AuthResponse(token, "Bearer", email, name, role, userId, "User registered successfully");
    }
}
