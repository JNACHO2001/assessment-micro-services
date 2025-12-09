package com.mycompany.microservice.auth.domain.port.in;

/**
 * Input port (use case) for user login.
 * This defines the contract that the application layer must implement.
 */
public interface LoginUserUseCase {

        /**
         * Authenticate a user and generate JWT token.
         * 
         * @param command containing login credentials
         * @return AuthResult with JWT token and user info
         * @throws InvalidCredentialsException if credentials are invalid
         */
        AuthResult login(LoginCommand command);

        /**
         * Command object containing login credentials.
         */
        record LoginCommand(
                        String email,
                        String password) {
        }

        /**
         * Result object containing authentication result.
         */
        record AuthResult(
                        String token,
                        String email,
                        String name,
                        String role,
                        Long userId) {
        }
}
