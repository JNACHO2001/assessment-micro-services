package com.mycompany.microservice.auth.domain.model;

/**
 * Domain enum representing user roles in the system.
 * Pure domain - no framework dependencies.
 */
public enum Role {
    ROLE_AFILIADO("Afiliado de la cooperativa"),
    ROLE_ANALISTA("Analista de crédito"),
    ROLE_ADMIN("Administrador del sistema");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
