package com.mycompany.microservice.auth.domain.port.out;

import com.mycompany.microservice.auth.domain.model.User;
import java.util.Optional;

/**
 * Output port for user persistence operations.
 * This is the interface that the domain defines for persistence.
 * Infrastructure layer will provide the implementation (adapter).
 */
public interface UserRepository {

    /**
     * Save a user to the persistence store.
     * 
     * @param user the user domain object to save
     * @return the saved user with generated id
     */
    User save(User user);

    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by document number.
     * 
     * @param document the document number to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByDocument(String document);

    /**
     * Find a user by ID.
     * 
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(Long id);

    /**
     * Check if a user exists with the given email.
     * 
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user exists with the given document.
     * 
     * @param document the document to check
     * @return true if exists, false otherwise
     */
    boolean existsByDocument(String document);
}
