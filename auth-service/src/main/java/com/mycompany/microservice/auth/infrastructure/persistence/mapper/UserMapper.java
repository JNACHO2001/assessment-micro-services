package com.mycompany.microservice.auth.infrastructure.persistence.mapper;

import com.mycompany.microservice.auth.domain.model.User;
import com.mycompany.microservice.auth.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between domain User and infrastructure UserEntity.
 * This maintains the separation between domain and infrastructure layers.
 */
@Component
public class UserMapper {

    /**
     * Convert domain User to JPA UserEntity.
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setDocument(user.getDocument());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setSalary(user.getSalary());
        entity.setAffiliationDate(user.getAffiliationDate());
        entity.setStatus(user.getStatus());
        entity.setRole(user.getRole());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());

        return entity;
    }

    /**
     * Convert JPA UserEntity to domain User.
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getDocument(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getSalary(),
                entity.getAffiliationDate(),
                entity.getStatus(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
