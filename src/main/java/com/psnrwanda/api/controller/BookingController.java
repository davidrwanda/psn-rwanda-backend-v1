package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.dto.PaginatedResponse;
import com.psnrwanda.api.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for booking operations
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "APIs for booking services")
@Slf4j
public class BookingController {
    
    private final BookingService bookingService;
    
    /**
     * Create a public booking (no authentication required)
     */
    @PostMapping("/public")
    @Operation(summary = "Create a public booking", description = "Create a booking without authentication")
    public ResponseEntity<BookingDto.BookingResponseDto> createPublicBooking(
            @Valid @RequestBody BookingDto.CreateBookingDto createBookingDto) {
        BookingDto.BookingResponseDto response = bookingService.createPublicBooking(createBookingDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Track bookings by phone number (no authentication required)
     */
    @PostMapping("/track")
    @Operation(summary = "Track bookings by phone number", description = "Get bookings by phone number without authentication")
    public ResponseEntity<PaginatedResponse<BookingDto>> trackBookingsByPhoneNumber(
            @Valid @RequestBody BookingDto.TrackBookingDto trackBookingDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookingDto> bookingsPage = bookingService.getBookingsByPhoneNumber(
                trackBookingDto.getPhoneNumber(), pageable);
        
        PaginatedResponse<BookingDto> response = PaginatedResponse.<BookingDto>builder()
                .data(bookingsPage.getContent())
                .currentPage(bookingsPage.getNumber())
                .totalItems(bookingsPage.getTotalElements())
                .totalPages(bookingsPage.getTotalPages())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Create an authenticated booking (requires authentication)
     */
    @PostMapping
    @Operation(
            summary = "Create an authenticated booking", 
            description = "Create a booking with authentication",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingDto.BookingResponseDto> createAuthenticatedBooking(
            @Valid @RequestBody BookingDto.CreateBookingDto createBookingDto) {
        BookingDto.BookingResponseDto response = bookingService.createAuthenticatedBooking(createBookingDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Get bookings for the authenticated user
     */
    @GetMapping("/my-bookings")
    @Operation(
            summary = "Get my bookings", 
            description = "Get bookings for the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaginatedResponse<BookingDto>> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookingDto> bookingsPage = bookingService.getMyBookings(pageable);
        
        PaginatedResponse<BookingDto> response = PaginatedResponse.<BookingDto>builder()
                .data(bookingsPage.getContent())
                .currentPage(bookingsPage.getNumber())
                .totalItems(bookingsPage.getTotalElements())
                .totalPages(bookingsPage.getTotalPages())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get booking details by ID (admin or owner only)
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get booking by ID", 
            description = "Get booking details by ID (admin or owner)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN') or @bookingAuthorization.isOwner(#id, authentication.principal.username)")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        BookingDto bookingDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDto);
    }
    
    /**
     * Update booking status (admin only)
     */
    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update booking status", 
            description = "Update booking status (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDto> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingDto.UpdateBookingStatusDto statusDto) {
        BookingDto updatedBooking = bookingService.updateBookingStatus(id, statusDto);
        return ResponseEntity.ok(updatedBooking);
    }
    
    /**
     * Get all bookings with pagination (admin only)
     */
    @GetMapping
    @Operation(
            summary = "Get all bookings", 
            description = "Get all bookings with pagination (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponse<BookingDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookingDto> bookingsPage;
        
        if (status != null && !status.isEmpty()) {
            bookingsPage = bookingService.getBookingsByStatus(status, pageable);
        } else {
            bookingsPage = bookingService.getAllBookings(pageable);
        }
        
        PaginatedResponse<BookingDto> response = PaginatedResponse.<BookingDto>builder()
                .data(bookingsPage.getContent())
                .currentPage(bookingsPage.getNumber())
                .totalItems(bookingsPage.getTotalElements())
                .totalPages(bookingsPage.getTotalPages())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete booking (admin only)
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete booking", 
            description = "Delete a booking (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Track bookings by phone number (for non-authenticated users)
     *
     * @param phoneNumber the phone number used for booking
     * @return list of bookings
     */
    @GetMapping("/track/phone/{phoneNumber}")
    @Operation(
        summary = "Track bookings by phone number",
        description = "Track service requests by phone number for non-authenticated users"
    )
    public ResponseEntity<List<BookingDto>> trackBookingsByPhoneNumber(
            @PathVariable String phoneNumber) {
        log.info("Tracking bookings for phone number: {}", phoneNumber);
        List<BookingDto> bookings = bookingService.findBookingsByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Track bookings by user ID (for authenticated users)
     *
     * @param userId the user ID
     * @return list of bookings
     */
    @GetMapping("/track/user/{userId}")
    @Operation(
        summary = "Track bookings by user ID",
        description = "Track service requests by user ID for authenticated users"
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BookingDto>> trackBookingsByUserId(
            @PathVariable Long userId) {
        log.info("Tracking bookings for user ID: {}", userId);
        List<BookingDto> bookings = bookingService.findBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Track booking by tracking number (for non-authenticated users)
     *
     * @param trackingNumber the tracking number of the booking
     * @return the booking
     */
    @GetMapping("/track/number/{trackingNumber}")
    @Operation(
        summary = "Track booking by tracking number",
        description = "Track service request by tracking number for non-authenticated users"
    )
    public ResponseEntity<BookingDto> trackBookingByTrackingNumber(
            @PathVariable String trackingNumber) {
        log.info("Tracking booking with tracking number: {}", trackingNumber);
        BookingDto booking = bookingService.getBookingByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(booking);
    }
} 