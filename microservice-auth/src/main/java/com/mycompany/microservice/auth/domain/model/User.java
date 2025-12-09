package com.mycompany.microservice.auth.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a User.
 * Pure domain - no framework dependencies (no JPA, no Spring annotations).
 * This is the core business entity following hexagonal architecture principles.
 */
public class User {

    private Long id;
    private String document;
    private String name;
    private String email;
    private String password;
    private BigDecimal salary;
    private LocalDateTime affiliationDate;
    private UserStatus status;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public User() {
    }

    // Constructor for new user registration (default role: AFILIADO)
    public User(String document, String name, String email, String password, BigDecimal salary) {
        this.document = document;
        this.name = name;
        this.email = email;
        this.password = password;
        this.salary = salary;
        this.affiliationDate = LocalDateTime.now();
        this.status = UserStatus.ACTIVO;
        this.role = Role.ROLE_AFILIADO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Full constructor
    public User(Long id, String document, String name, String email, String password,
            BigDecimal salary, LocalDateTime affiliationDate, UserStatus status, Role role,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.email = email;
        this.password = password;
        this.salary = salary;
        this.affiliationDate = affiliationDate;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Domain behavior methods
    public boolean isActive() {
        return this.status == UserStatus.ACTIVO;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVO;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = UserStatus.ACTIVO;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDateTime getAffiliationDate() {
        return affiliationDate;
    }

    public void setAffiliationDate(LocalDateTime affiliationDate) {
        this.affiliationDate = affiliationDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
