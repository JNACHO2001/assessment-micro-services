package com.mycompany.microservice.credit.infrastructure.persistence.mapper;

import com.mycompany.microservice.credit.domain.model.CreditApplication;
import com.mycompany.microservice.credit.infrastructure.persistence.entity.CreditApplicationEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between domain CreditApplication and JPA CreditApplicationEntity.
 */
@Component
public class CreditApplicationMapper {

    public CreditApplicationEntity toEntity(CreditApplication domain) {
        if (domain == null)
            return null;

        CreditApplicationEntity entity = new CreditApplicationEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setAmount(domain.getAmount());
        entity.setTermMonths(domain.getTermMonths());
        entity.setPurpose(domain.getPurpose());
        entity.setStatus(domain.getStatus());
        entity.setAnalystNotes(domain.getAnalystNotes());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public CreditApplication toDomain(CreditApplicationEntity entity) {
        if (entity == null)
            return null;

        return new CreditApplication(
                entity.getId(),
                entity.getUserId(),
                entity.getAmount(),
                entity.getTermMonths(),
                entity.getPurpose(),
                entity.getStatus(),
                entity.getAnalystNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
