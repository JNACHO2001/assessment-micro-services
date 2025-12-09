package com.mycompany.microservice.auth.application.service;

import com.mycompany.microservice.auth.domain.exception.InvalidCredentialsException;
import com.mycompany.microservice.auth.domain.model.User;
import com.mycompany.microservice.auth.domain.port.in.LoginUserUseCase;
import com.mycompany.microservice.auth.domain.port.out.UserRepository;
import com.mycompany.microservice.auth.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application service implementing the LoginUserUseCase.
 * This is part of the application layer in hexagonal architecture.
 */
public class LoginUserService implements LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResult login(LoginCommand command) {
        // Find user by email
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        // Verify password
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new InvalidCredentialsException("User account is deactivated");
        }

        // Generate JWT token with role
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        return new AuthResult(token, user.getEmail(), user.getName(), user.getRole().name(), user.getId());
    }
}
