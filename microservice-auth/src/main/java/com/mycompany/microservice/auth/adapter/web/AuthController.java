package com.mycompany.microservice.auth.adapter.web;

import com.mycompany.microservice.auth.application.dto.AuthResponse;
import com.mycompany.microservice.auth.application.dto.LoginRequest;
import com.mycompany.microservice.auth.application.dto.RegisterRequest;
import com.mycompany.microservice.auth.domain.model.User;
import com.mycompany.microservice.auth.domain.port.in.LoginUserUseCase;
import com.mycompany.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.mycompany.microservice.auth.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication endpoints.
 * This is the adapter that exposes the use cases via HTTP.
 * Part of the adapter layer in hexagonal architecture.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final RegisterUserUseCase registerUserUseCase;
        private final LoginUserUseCase loginUserUseCase;
        private final JwtTokenProvider jwtTokenProvider;
        private final com.mycompany.microservice.auth.domain.port.out.UserRepository userRepository;

        public AuthController(RegisterUserUseCase registerUserUseCase,
                        LoginUserUseCase loginUserUseCase,
                        JwtTokenProvider jwtTokenProvider,
                        com.mycompany.microservice.auth.domain.port.out.UserRepository userRepository) {
                this.registerUserUseCase = registerUserUseCase;
                this.loginUserUseCase = loginUserUseCase;
                this.jwtTokenProvider = jwtTokenProvider;
                this.userRepository = userRepository;
        }

        /**
         * Register a new user.
         * POST /api/auth/register
         */
        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
                // Convert DTO to use case command
                RegisterUserUseCase.RegisterCommand command = new RegisterUserUseCase.RegisterCommand(
                                request.document(),
                                request.name(),
                                request.email(),
                                request.password(),
                                request.salary());

                // Execute use case
                User registeredUser = registerUserUseCase.register(command);

                // Generate token for the new user (includes role)
                String token = jwtTokenProvider.generateToken(
                                registeredUser.getEmail(),
                                registeredUser.getId(),
                                registeredUser.getRole().name());

                // Build response
                AuthResponse response = AuthResponse.registered(
                                token,
                                registeredUser.getEmail(),
                                registeredUser.getName(),
                                registeredUser.getRole().name(),
                                registeredUser.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * Login user.
         * POST /api/auth/login
         */
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
                // Convert DTO to use case command
                LoginUserUseCase.LoginCommand command = new LoginUserUseCase.LoginCommand(
                                request.email(),
                                request.password());

                // Execute use case
                LoginUserUseCase.AuthResult result = loginUserUseCase.login(command);

                // Build response
                AuthResponse response = AuthResponse.success(
                                result.token(),
                                result.email(),
                                result.name(),
                                result.role(),
                                result.userId());

                return ResponseEntity.ok(response);
        }

        /**
         * Health check endpoint.
         * GET /api/auth/health
         */
        @GetMapping("/health")
        public ResponseEntity<String> health() {
                return ResponseEntity.ok("Auth service is running");
        }

        /**
         * Get user by ID (for internal microservice calls).
         * GET /api/auth/users/{id}
         */
        @GetMapping("/users/{id}")
        public ResponseEntity<AuthResponse> getUserById(@PathVariable Long id) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                AuthResponse response = AuthResponse.success(
                                null, // No token needed for internal call
                                user.getEmail(),
                                user.getName(),
                                user.getRole().name(),
                                user.getId());

                return ResponseEntity.ok(response);
        }
}
