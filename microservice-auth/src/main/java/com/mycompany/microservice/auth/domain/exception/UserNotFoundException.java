package com.mycompany.microservice.auth.domain.exception;

/**
 * Domain exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("User with email '" + email + "' not found");
    }

    public static UserNotFoundException byId(Long id) {
        return new UserNotFoundException("User with id '" + id + "' not found");
    }
}
