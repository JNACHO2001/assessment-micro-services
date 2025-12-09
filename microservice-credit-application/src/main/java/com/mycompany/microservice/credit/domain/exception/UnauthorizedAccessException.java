package com.mycompany.microservice.credit.domain.exception;

/**
 * Exception thrown when user doesn't have permission to perform an action.
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public static UnauthorizedAccessException notOwner() {
        return new UnauthorizedAccessException("You don't have permission to access this application");
    }

    public static UnauthorizedAccessException insufficientRole(String requiredRole) {
        return new UnauthorizedAccessException("Requires role: " + requiredRole);
    }
}
