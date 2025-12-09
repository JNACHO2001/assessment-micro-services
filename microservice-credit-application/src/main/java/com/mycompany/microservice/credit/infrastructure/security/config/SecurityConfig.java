package com.mycompany.microservice.credit.infrastructure.security.config;

import com.mycompany.microservice.credit.infrastructure.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration with role-based access control.
 * 
 * Access Rules:
 * - AFILIADO: GET /my, POST (create), DELETE own applications
 * - ANALISTA: Full CRUD
 * - ADMIN: Full CRUD
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Health check - public
                        .requestMatchers("/api/applications/health").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // My applications - AFILIADO can only see their own
                        .requestMatchers(HttpMethod.GET, "/api/applications/my")
                        .hasAnyAuthority("ROLE_AFILIADO", "ROLE_ANALISTA", "ROLE_ADMIN")

                        // Create application - All authenticated users
                        .requestMatchers(HttpMethod.POST, "/api/applications")
                        .hasAnyAuthority("ROLE_AFILIADO", "ROLE_ANALISTA", "ROLE_ADMIN")

                        // Get all applications - ANALISTA and ADMIN only
                        .requestMatchers(HttpMethod.GET, "/api/applications")
                        .hasAnyAuthority("ROLE_ANALISTA", "ROLE_ADMIN")

                        // Get by ID - role check done in service
                        .requestMatchers(HttpMethod.GET, "/api/applications/{id}")
                        .hasAnyAuthority("ROLE_AFILIADO", "ROLE_ANALISTA", "ROLE_ADMIN")

                        // Update application - ANALISTA and ADMIN only
                        .requestMatchers(HttpMethod.PUT, "/api/applications/**")
                        .hasAnyAuthority("ROLE_ANALISTA", "ROLE_ADMIN")

                        // Update status - ADMIN only
                        .requestMatchers(HttpMethod.PATCH, "/api/applications/*/status").hasAuthority("ROLE_ADMIN")

                        // Delete - role check done in service (AFILIADO can only delete own pending)
                        .requestMatchers(HttpMethod.DELETE, "/api/applications/**")
                        .hasAnyAuthority("ROLE_AFILIADO", "ROLE_ANALISTA", "ROLE_ADMIN")

                        // All other requests require authentication
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
