package com.mycompany.microservice.credit.domain.model;

/**
 * Domain enum representing credit application status.
 */
public enum ApplicationStatus {
    PENDIENTE("Solicitud pendiente de revisión"),
    EN_REVISION("Solicitud en proceso de análisis"),
    APROBADA("Solicitud aprobada"),
    RECHAZADA("Solicitud rechazada");
    
    private final String description;
    
    ApplicationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
