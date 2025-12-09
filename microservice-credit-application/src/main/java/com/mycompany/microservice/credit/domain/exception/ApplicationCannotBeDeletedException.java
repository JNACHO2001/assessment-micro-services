package com.mycompany.microservice.credit.domain.exception;

/**
 * Exception thrown when an application cannot be deleted.
 */
public class ApplicationCannotBeDeletedException extends RuntimeException {

    public ApplicationCannotBeDeletedException(String message) {
        super(message);
    }

    public static ApplicationCannotBeDeletedException notPending() {
        return new ApplicationCannotBeDeletedException("Only pending applications can be deleted");
    }
}
