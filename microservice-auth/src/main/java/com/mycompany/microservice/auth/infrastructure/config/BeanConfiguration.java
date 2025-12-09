package com.mycompany.microservice.auth.infrastructure.config;

import com.mycompany.microservice.auth.application.service.LoginUserService;
import com.mycompany.microservice.auth.application.service.RegisterUserService;
import com.mycompany.microservice.auth.domain.port.in.LoginUserUseCase;
import com.mycompany.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.mycompany.microservice.auth.domain.port.out.UserRepository;
import com.mycompany.microservice.auth.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean Configuration for hexagonal architecture.
 * Wires up the application services with their dependencies.
 * This is where we connect the ports to their adapters.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Create RegisterUserUseCase implementation.
     * Connects the input port to its service implementation.
     */
    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return new RegisterUserService(userRepository, passwordEncoder);
    }

    /**
     * Create LoginUserUseCase implementation.
     * Connects the input port to its service implementation.
     */
    @Bean
    public LoginUserUseCase loginUserUseCase(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        return new LoginUserService(userRepository, passwordEncoder, jwtTokenProvider);
    }
}
