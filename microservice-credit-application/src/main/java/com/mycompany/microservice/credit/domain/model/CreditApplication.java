package com.mycompany.microservice.credit.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a Credit Application.
 * Pure domain - no framework dependencies.
 */
public class CreditApplication {

    private Long id;
    private Long userId;
    private BigDecimal amount;
    private Integer termMonths;
    private String purpose;
    private ApplicationStatus status;
    private String analystNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreditApplication() {
    }

    // Constructor for new application
    public CreditApplication(Long userId,
            BigDecimal amount, Integer termMonths, String purpose) {
        this.userId = userId;
        this.amount = amount;
        this.termMonths = termMonths;
        this.purpose = purpose;
        this.status = ApplicationStatus.PENDIENTE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Full constructor
    public CreditApplication(Long id, Long userId,
            BigDecimal amount, Integer termMonths, String purpose,
            ApplicationStatus status, String analystNotes,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.termMonths = termMonths;
        this.purpose = purpose;
        this.status = status;
        this.analystNotes = analystNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Domain behavior
    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean canBeDeleted() {
        return this.status == ApplicationStatus.PENDIENTE;
    }

    public void approve(String notes) {
        this.status = ApplicationStatus.APROBADA;
        this.analystNotes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(String notes) {
        this.status = ApplicationStatus.RECHAZADA;
        this.analystNotes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public void startReview() {
        this.status = ApplicationStatus.EN_REVISION;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getAnalystNotes() {
        return analystNotes;
    }

    public void setAnalystNotes(String analystNotes) {
        this.analystNotes = analystNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
