package com.psnrwanda.api.security;

import com.psnrwanda.api.model.Booking;
import com.psnrwanda.api.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service for booking authorization checks
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingAuthorization {
    
    private final BookingRepository bookingRepository;
    
    /**
     * Check if the given username is the owner of the booking
     * 
     * @param bookingId The booking ID
     * @param username The username to check
     * @return true if the user is the owner, false otherwise
     */
    public boolean isOwner(Long bookingId, String username) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            log.debug("Booking with id {} not found", bookingId);
            return false;
        }
        
        Booking booking = bookingOpt.get();
        
        if (booking.getUser() == null) {
            log.debug("Booking with id {} has no user associated", bookingId);
            return false;
        }
        
        return booking.getUser().getUsername().equals(username);
    }
} 