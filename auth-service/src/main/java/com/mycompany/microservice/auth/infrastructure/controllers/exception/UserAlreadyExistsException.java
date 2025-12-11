package com.mycompany.microservice.auth.infrastructure.controllers.exception;

/**
 * Domain exception thrown when attempting to register a user
 * with an email or document that already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("User with email '" + email + "' already exists");
    }

    public static UserAlreadyExistsException byDocument(String document) {
        return new UserAlreadyExistsException("User with document '" + document + "' already exists");
    }
}
