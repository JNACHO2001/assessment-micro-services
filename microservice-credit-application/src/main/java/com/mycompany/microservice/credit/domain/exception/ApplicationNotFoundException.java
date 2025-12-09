package com.mycompany.microservice.credit.domain.exception;

/**
 * Exception thrown when a credit application is not found.
 */
public class ApplicationNotFoundException extends RuntimeException {
    
    public ApplicationNotFoundException(String message) {
        super(message);
    }
    
    public static ApplicationNotFoundException byId(Long id) {
        return new ApplicationNotFoundException("Credit application with id '" + id + "' not found");
    }
}
