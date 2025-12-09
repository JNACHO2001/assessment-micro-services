package com.mycompany.microservice.credit.infrastructure.persistence.repository;

import com.mycompany.microservice.credit.infrastructure.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for CreditApplicationEntity.
 */
@Repository
public interface JpaCreditApplicationRepository extends JpaRepository<CreditApplicationEntity, Long> {

    List<CreditApplicationEntity> findByUserId(Long userId);

    List<CreditApplicationEntity> findByStatus(String status);
}
