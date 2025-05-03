package com.psnrwanda.api.repository;

import com.psnrwanda.api.model.Booking;
import com.psnrwanda.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find all bookings by user
     * @param user The user
     * @param pageable Pagination information
     * @return Page of bookings
     */
    Page<Booking> findByUser(User user, Pageable pageable);
    
    /**
     * Find all bookings by phone number
     * @param phoneNumber The phone number
     * @param pageable Pagination information
     * @return Page of bookings
     */
    Page<Booking> findByPhoneNumber(String phoneNumber, Pageable pageable);
    
    /**
     * Find all bookings by status
     * @param status The booking status
     * @param pageable Pagination information
     * @return Page of bookings
     */
    Page<Booking> findByStatus(Booking.BookingStatus status, Pageable pageable);
    
    /**
     * Find all bookings by service ID
     * @param serviceId The service ID
     * @return List of bookings
     */
    List<Booking> findByServiceId(Long serviceId);
    
    /**
     * Count bookings by status
     * @param status The booking status
     * @return Number of bookings
     */
    Long countByStatus(Booking.BookingStatus status);
    
    /**
     * Find bookings by phone number ordered by creation date descending
     *
     * @param phoneNumber the phone number
     * @return list of bookings
     */
    List<Booking> findByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
    
    /**
     * Find bookings by user ID ordered by creation date descending
     *
     * @param userId the user ID
     * @return list of bookings
     */
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Find the booking with the highest tracking number
     * @return Optional booking with the highest tracking number
     */
    Optional<Booking> findTopByOrderByTrackingNumberDesc();
    
    /**
     * Find a booking by its tracking number
     * @param trackingNumber The tracking number
     * @return Optional booking
     */
    Optional<Booking> findByTrackingNumber(String trackingNumber);
} 