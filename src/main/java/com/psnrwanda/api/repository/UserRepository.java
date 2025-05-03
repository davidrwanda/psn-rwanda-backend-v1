package com.psnrwanda.api.repository;

import com.psnrwanda.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     *
     * @param username username
     * @return optional of user
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     *
     * @param email email
     * @return optional of user
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by username
     *
     * @param username username
     * @return true if exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email
     *
     * @param email email
     * @return true if exists
     */
    boolean existsByEmail(String email);
} 