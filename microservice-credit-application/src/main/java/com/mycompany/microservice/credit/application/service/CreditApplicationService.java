package com.mycompany.microservice.credit.application.service;

import com.mycompany.microservice.credit.domain.exception.ApplicationCannotBeDeletedException;
import com.mycompany.microservice.credit.domain.exception.ApplicationNotFoundException;
import com.mycompany.microservice.credit.domain.exception.UnauthorizedAccessException;
import com.mycompany.microservice.credit.domain.model.ApplicationStatus;
import com.mycompany.microservice.credit.domain.model.CreditApplication;
import com.mycompany.microservice.credit.domain.port.out.CreditApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service for credit application operations.
 * Implements role-based access control:
 * - AFILIADO: can only view their own applications
 * - ANALISTA/ADMIN: full CRUD access
 */
@Service
@Transactional
public class CreditApplicationService {

    private final CreditApplicationRepository repository;

    public CreditApplicationService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new credit application (AFILIADO only).
     */
    public CreditApplication create(Long userId, String userEmail, String userName,
            BigDecimal amount, Integer termMonths, String purpose) {
        CreditApplication application = new CreditApplication(
                userId, userEmail, userName, amount, termMonths, purpose);
        return repository.save(application);
    }

    /**
     * Get applications for a specific user (AFILIADO).
     */
    public List<CreditApplication> getMyApplications(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Get all applications (ANALISTA, ADMIN only).
     */
    public List<CreditApplication> getAllApplications() {
        return repository.findAll();
    }

    /**
     * Get application by ID.
     * AFILIADO can only access their own applications.
     */
    public CreditApplication getById(Long id, Long userId, String role) {
        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(id));

        // AFILIADO can only see their own applications
        if ("ROLE_AFILIADO".equals(role) && !application.isOwnedBy(userId)) {
            throw UnauthorizedAccessException.notOwner();
        }

        return application;
    }

    /**
     * Delete application (AFILIADO owner, or ANALISTA/ADMIN).
     */
    public void delete(Long id, Long userId, String role) {
        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(id));

        // AFILIADO can only delete their own pending applications
        if ("ROLE_AFILIADO".equals(role)) {
            if (!application.isOwnedBy(userId)) {
                throw UnauthorizedAccessException.notOwner();
            }
            if (!application.canBeDeleted()) {
                throw ApplicationCannotBeDeletedException.notPending();
            }
        }

        repository.deleteById(id);
    }

    /**
     * Update application (ANALISTA, ADMIN only).
     */
    public CreditApplication update(Long id, BigDecimal interestRate,
            String analystNotes, String statusStr) {
        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(id));

        if (interestRate != null) {
            application.setInterestRate(interestRate);
        }
        if (analystNotes != null) {
            application.setAnalystNotes(analystNotes);
        }
        if (statusStr != null) {
            ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
            application.setStatus(status);
        }
        application.setUpdatedAt(LocalDateTime.now());

        return repository.save(application);
    }

    /**
     * Update status only (ADMIN only).
     */
    public CreditApplication updateStatus(Long id, String statusStr) {
        CreditApplication application = repository.findById(id)
                .orElseThrow(() -> ApplicationNotFoundException.byId(id));

        ApplicationStatus status = ApplicationStatus.valueOf(statusStr);
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        return repository.save(application);
    }
}
