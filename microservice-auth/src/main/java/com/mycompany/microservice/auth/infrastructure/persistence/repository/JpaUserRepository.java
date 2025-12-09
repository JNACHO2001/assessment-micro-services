package com.mycompany.microservice.auth.infrastructure.persistence.repository;

import com.mycompany.microservice.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for UserEntity.
 * Infrastructure layer - framework-specific implementation.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByDocument(String document);

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
