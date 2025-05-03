package com.psnrwanda.api.repository;

import com.psnrwanda.api.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Company entity
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // No additional methods needed at this time
} 