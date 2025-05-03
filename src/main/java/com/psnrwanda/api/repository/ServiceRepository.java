package com.psnrwanda.api.repository;

import com.psnrwanda.api.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Service entity
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    /**
     * Find all active services
     * @return List of active services
     */
    List<Service> findByActiveTrue();
    
    /**
     * Find service by title
     * @param title Service title
     * @return Service if found
     */
    List<Service> findByTitleContainingIgnoreCase(String title);
} 