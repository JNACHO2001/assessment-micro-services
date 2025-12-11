package com.mycompany.microservice.auth.infrastructure.controllers.exception;

/**
 * Domain exception thrown when login credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
