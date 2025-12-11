package com.mycompany.microservice.credit.infrastructure.controllers;

import com.mycompany.microservice.credit.application.dto.ApplicationResponse;
import com.mycompany.microservice.credit.application.dto.CreateApplicationRequest;
import com.mycompany.microservice.credit.application.dto.UpdateApplicationRequest;
import com.mycompany.microservice.credit.application.service.CreditApplicationService;
import com.mycompany.microservice.credit.domain.model.CreditApplication;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for Credit Application endpoints.
 * 
 * Access Rules:
 * - AFILIADO: Create, view own, delete own pending applications
 * - ANALISTA: Full CRUD
 * - ADMIN: Full CRUD
 */
@RestController
@RequestMapping("/api/applications")
public class CreditApplicationController {

    private final CreditApplicationService service;
    

    public CreditApplicationController(CreditApplicationService service) {
        this.service = service;
    }

    /**
     * Create a new credit application.
     * POST /api/applications
     */
    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody CreateApplicationRequest request,
            
            Authentication authentication) {

        Map<String, Object> userDetails = getUserDetails(authentication);
        Long userId = (Long) userDetails.get("userId");

        // For AFILIADO, use their own info. For others, could be on behalf of user
        CreditApplication created = service.create(
                userId,
                request.amount(), request.termMonths(), request.purpose());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApplicationResponse.from(created));
    }

    /**
     * Get my applications (for AFILIADO).
     * GET /api/applications/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(Authentication authentication) {
        Long userId = getUserId(authentication);

        List<ApplicationResponse> applications = service.getMyApplications(userId).stream()
                .map(ApplicationResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(applications);
    }

    /**
     * Get all applications (ANALISTA, ADMIN only).
     * GET /api/applications
     */
    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        List<ApplicationResponse> applications = service.getAllApplications().stream()
                .map(ApplicationResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(applications);
    }

    /**
     * Get application by ID.
     * GET /api/applications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String role = getRole(authentication);

        CreditApplication application = service.getById(id, userId, role);
        return ResponseEntity.ok(ApplicationResponse.from(application));
    }

    /**
     * Update application (ANALISTA, ADMIN only).
     * PUT /api/applications/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationRequest request) {

        CreditApplication updated = service.update(
                id, request.analystNotes(), request.status());

        return ResponseEntity.ok(ApplicationResponse.from(updated));
    }

    /**
     * Update status only (ADMIN only).
     * PATCH /api/applications/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        CreditApplication updated = service.updateStatus(id, status);

        return ResponseEntity.ok(ApplicationResponse.from(updated));
    }

    /**
     * Delete application.
     * DELETE /api/applications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = getUserId(authentication);
        String role = getRole(authentication);

        service.delete(id, userId, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check.
     * GET /api/applications/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Credit Application service is running");
    }

    // Helper methods
    @SuppressWarnings("unchecked")
    private Map<String, Object> getUserDetails(Authentication authentication) {
        return (Map<String, Object>) authentication.getPrincipal();
    }

    private Long getUserId(Authentication authentication) {
        return (Long) getUserDetails(authentication).get("userId");
    }

    private String getRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_AFILIADO");
    }
}
