package com.mycompany.microservice.auth.infrastructure.persistence.adapter;

import com.mycompany.microservice.auth.domain.model.User;
import com.mycompany.microservice.auth.domain.port.out.UserRepository;
import com.mycompany.microservice.auth.infrastructure.persistence.entity.UserEntity;
import com.mycompany.microservice.auth.infrastructure.persistence.mapper.UserMapper;
import com.mycompany.microservice.auth.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter that implements the domain UserRepository port.
 * This is the bridge between domain and infrastructure.
 * Following hexagonal architecture: infrastructure implements domain
 * interfaces.
 */
@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return jpaUserRepository.findByDocument(document)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDocument(String document) {
        return jpaUserRepository.existsByDocument(document);
    }
}
