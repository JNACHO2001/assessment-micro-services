package com.mycompany.microservice.auth.domain.port.in;

import com.mycompany.microservice.auth.domain.model.User;

/**
 * Input port (use case) for user registration.
 * This defines the contract that the application layer must implement.
 */
public interface RegisterUserUseCase {

    /**
     * Register a new user in the system.
     * 
     * @param command containing registration data
     * @return the registered user
     * @throws UserAlreadyExistsException if email or document already exists
     */
    User register(RegisterCommand command);

    /**
     * Command object containing registration data.
     * Using a record for immutability.
     */
    record RegisterCommand(
            String document,
            String name,
            String email,
            String password,
            String salary) {
    }
}
