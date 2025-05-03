package com.psnrwanda.api.repository;

import com.psnrwanda.api.model.BookingDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for BookingDocument entity
 */
@Repository
public interface BookingDocumentRepository extends JpaRepository<BookingDocument, Long> {
    
    /**
     * Find all documents for a specific booking
     * @param bookingId The booking ID
     * @return List of documents
     */
    List<BookingDocument> findByBookingId(Long bookingId);
} 