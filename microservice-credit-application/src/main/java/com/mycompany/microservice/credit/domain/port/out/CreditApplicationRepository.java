package com.mycompany.microservice.credit.domain.port.out;

import com.mycompany.microservice.credit.domain.model.CreditApplication;
import java.util.List;
import java.util.Optional;

/**
 * Output port for credit application persistence.
 */
public interface CreditApplicationRepository {
    
    CreditApplication save(CreditApplication application);
    
    Optional<CreditApplication> findById(Long id);
    
    List<CreditApplication> findByUserId(Long userId);
    
    List<CreditApplication> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}
