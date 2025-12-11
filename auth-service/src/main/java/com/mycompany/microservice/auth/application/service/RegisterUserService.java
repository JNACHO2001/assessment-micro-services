package com.mycompany.microservice.auth.application.service;

import com.mycompany.microservice.auth.infrastructure.controllers.exception.UserAlreadyExistsException;
import com.mycompany.microservice.auth.domain.model.User;
import com.mycompany.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.mycompany.microservice.auth.domain.port.out.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Application service implementing the RegisterUserUseCase.
 * This is part of the application layer in hexagonal architecture.
 */
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(RegisterCommand command) {
        // Validate that email doesn't already exist
        if (userRepository.existsByEmail(command.email())) {
            throw UserAlreadyExistsException.byEmail(command.email());
        }

        // Validate that document doesn't already exist
        if (userRepository.existsByDocument(command.document())) {
            throw UserAlreadyExistsException.byDocument(command.document());
        }

        // Parse salary
        BigDecimal salary = parseSalary(command.salary());

        // Create domain user with encrypted password
        User user = new User(
                command.document(),
                command.name(),
                command.email(),
                passwordEncoder.encode(command.password()),
                salary);

        // Persist and return
        return userRepository.save(user);
    }

    private BigDecimal parseSalary(String salary) {
        try {
            return new BigDecimal(salary.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
