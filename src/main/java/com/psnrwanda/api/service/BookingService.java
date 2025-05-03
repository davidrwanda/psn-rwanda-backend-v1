package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.exception.ResourceNotFoundException;
import com.psnrwanda.api.model.Booking;
import com.psnrwanda.api.model.BookingDocument;
import com.psnrwanda.api.model.Service;
import com.psnrwanda.api.model.User;
import com.psnrwanda.api.repository.BookingDocumentRepository;
import com.psnrwanda.api.repository.BookingRepository;
import com.psnrwanda.api.repository.ServiceRepository;
import com.psnrwanda.api.repository.UserRepository;
import com.psnrwanda.api.service.common.AbstractCrudService;
import com.psnrwanda.api.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for Booking entity
 */
@Slf4j
@Component
@Transactional
public class BookingService extends AbstractCrudService<Booking, BookingRepository> {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String TRACKING_NUMBER_PREFIX = "PSN-";
    private static final Object TRACKING_NUMBER_LOCK = new Object();
    
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final BookingDocumentRepository bookingDocumentRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;
    
    /**
     * Constructor
     * @param repository Booking repository
     * @param serviceRepository Service repository
     * @param userRepository User repository
     * @param bookingDocumentRepository Booking document repository
     * @param fileStorageService File storage service
     * @param notificationService Notification service
     */
    public BookingService(BookingRepository repository, 
                         ServiceRepository serviceRepository, 
                         UserRepository userRepository,
                         BookingDocumentRepository bookingDocumentRepository,
                         FileStorageService fileStorageService,
                         NotificationService notificationService) {
        super(repository, "Booking");
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.bookingDocumentRepository = bookingDocumentRepository;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }
    
    /**
     * Generate a new tracking number for bookings
     * @return The generated tracking number in format PSN-001
     */
    private String generateTrackingNumber() {
        synchronized (TRACKING_NUMBER_LOCK) {
            // Find the highest tracking number
            String highestTrackingNumber = repository.findTopByOrderByTrackingNumberDesc()
                    .map(Booking::getTrackingNumber)
                    .orElse(null);
            
            int nextNumber = 1;
            if (highestTrackingNumber != null && highestTrackingNumber.startsWith(TRACKING_NUMBER_PREFIX)) {
                try {
                    String numberPart = highestTrackingNumber.substring(TRACKING_NUMBER_PREFIX.length());
                    nextNumber = Integer.parseInt(numberPart) + 1;
                } catch (NumberFormatException e) {
                    log.warn("Could not parse tracking number: {}", highestTrackingNumber);
                }
            }
            
            // Format the number with leading zeros: PSN-001, PSN-002, etc.
            return String.format("%s%03d", TRACKING_NUMBER_PREFIX, nextNumber);
        }
    }
    
    @Override
    public Booking create(Booking booking) {
        // Generate tracking number
        if (booking.getTrackingNumber() == null) {
            booking.setTrackingNumber(generateTrackingNumber());
        }
        
        return super.create(booking);
    }
    
    /**
     * Create a booking for a non-authenticated user (public booking)
     * @param createBookingDto Create booking DTO
     * @return Booking response DTO
     */
    public BookingDto.BookingResponseDto createPublicBooking(BookingDto.CreateBookingDto createBookingDto) {
        // Get service
        Service service = serviceRepository.findById(createBookingDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", createBookingDto.getServiceId()));
        
        // Check if service is active
        if (!service.isActive()) {
            throw new IllegalArgumentException("The selected service is not available at the moment");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setPhoneNumber(createBookingDto.getPhoneNumber());
        booking.setService(service);
        booking.setEmail(createBookingDto.getEmail());
        booking.setFullName(createBookingDto.getFullName());
        booking.setNotes(createBookingDto.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        // Check if user is authenticated and associate booking with user
        Optional<User> currentUser = getCurrentUser();
        currentUser.ifPresent(booking::setUser);
        
        // Save booking (tracking number will be generated in create method)
        Booking savedBooking = create(booking);
        
        // Process document IDs if present
        if (createBookingDto.getDocumentIds() != null && !createBookingDto.getDocumentIds().isEmpty()) {
            for (String documentId : createBookingDto.getDocumentIds()) {
                try {
                    BookingDocument document = bookingDocumentRepository.findById(Long.parseLong(documentId))
                            .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));
                    savedBooking.addDocument(document);
                } catch (NumberFormatException e) {
                    log.error("Invalid document ID: {}", documentId);
                }
            }
            savedBooking = update(savedBooking.getId(), savedBooking);
        }
        
        // Convert to response DTO
        BookingDto bookingDto = convertToDto(savedBooking);
        
        // Send notifications to both client and provider
        if (createBookingDto.getEmail() != null && !createBookingDto.getEmail().isEmpty()) {
            try {
                notificationService.sendBookingConfirmationWithProviderNotification(bookingDto, createBookingDto.getEmail());
            } catch (Exception e) {
                log.error("Failed to send booking notifications: {}", e.getMessage());
                // Don't prevent booking creation if notification fails
            }
        }
        
        return BookingDto.BookingResponseDto.builder()
                .booking(bookingDto)
                .message("Thank you! Your booking has been submitted successfully. Your tracking number is " + savedBooking.getTrackingNumber() + ". We will contact you shortly.")
                .build();
    }
    
    /**
     * Upload documents for a booking
     * @param files The files to upload
     * @return List of booking document DTOs
     */
    public List<BookingDto.BookingDocumentDto> uploadDocuments(List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    BookingDocument document = fileStorageService.storeFile(file);
                    document = bookingDocumentRepository.save(document);
                    return convertToDocumentDto(document);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Create a booking for an authenticated user
     * @param createBookingDto Create booking DTO
     * @return Booking response DTO
     */
    public BookingDto.BookingResponseDto createAuthenticatedBooking(BookingDto.CreateBookingDto createBookingDto) {
        // Get current user
        User user = getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
        
        // Get service
        Service service = serviceRepository.findById(createBookingDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", createBookingDto.getServiceId()));
        
        // Check if service is active
        if (!service.isActive()) {
            throw new IllegalArgumentException("The selected service is not available at the moment");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setPhoneNumber(createBookingDto.getPhoneNumber());
        booking.setService(service);
        booking.setEmail(createBookingDto.getEmail());
        booking.setFullName(createBookingDto.getFullName());
        booking.setNotes(createBookingDto.getNotes());
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setUser(user);
        
        // Save booking (tracking number will be generated in create method)
        Booking savedBooking = create(booking);
        
        // Process document IDs if present
        if (createBookingDto.getDocumentIds() != null && !createBookingDto.getDocumentIds().isEmpty()) {
            for (String documentId : createBookingDto.getDocumentIds()) {
                try {
                    BookingDocument document = bookingDocumentRepository.findById(Long.parseLong(documentId))
                            .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));
                    savedBooking.addDocument(document);
                } catch (NumberFormatException e) {
                    log.error("Invalid document ID: {}", documentId);
                }
            }
            savedBooking = update(savedBooking.getId(), savedBooking);
        }
        
        // Convert to response DTO
        BookingDto bookingDto = convertToDto(savedBooking);
        
        // Send notifications to both client and provider
        if (createBookingDto.getEmail() != null && !createBookingDto.getEmail().isEmpty()) {
            try {
                notificationService.sendBookingConfirmationWithProviderNotification(bookingDto, createBookingDto.getEmail());
            } catch (Exception e) {
                log.error("Failed to send booking notifications: {}", e.getMessage());
                // Don't prevent booking creation if notification fails
            }
        }
        
        return BookingDto.BookingResponseDto.builder()
                .booking(bookingDto)
                .message("Thank you! Your booking has been submitted successfully. Your tracking number is " + savedBooking.getTrackingNumber() + ". You can track it in your account.")
                .build();
    }
    
    /**
     * Get bookings for current authenticated user
     * @param pageable Pagination information
     * @return Page of booking DTOs
     */
    @Transactional(readOnly = true)
    public Page<BookingDto> getMyBookings(Pageable pageable) {
        // Get current user
        User user = getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
        
        return repository.findByUser(user, pageable)
                .map(this::convertToDto);
    }
    
    /**
     * Update booking status
     * @param id Booking ID
     * @param statusDto Status update DTO
     * @return Updated booking DTO
     */
    public BookingDto updateBookingStatus(Long id, BookingDto.UpdateBookingStatusDto statusDto) {
        Booking booking = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        
        try {
            Booking.BookingStatus newStatus = Booking.BookingStatus.valueOf(statusDto.getStatus().toUpperCase());
            booking.setStatus(newStatus);
            
            if (statusDto.getNotes() != null) {
                booking.setNotes(statusDto.getNotes());
            }
            
            Booking updatedBooking = update(id, booking);
            return convertToDto(updatedBooking);
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusDto.getStatus());
        }
    }
    
    /**
     * Get booking details by ID
     * @param id Booking ID
     * @return Booking DTO
     */
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long id) {
        return findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
    }
    
    /**
     * Get bookings by phone number for public tracking
     * @param phoneNumber Phone number
     * @param pageable Pagination information
     * @return Page of booking DTOs
     */
    @Transactional(readOnly = true)
    public Page<BookingDto> getBookingsByPhoneNumber(String phoneNumber, Pageable pageable) {
        return repository.findByPhoneNumber(phoneNumber, pageable)
                .map(this::convertToDto);
    }
    
    /**
     * Get all bookings with pagination (admin only)
     * @param pageable Pagination information
     * @return Page of booking DTOs
     */
    @Transactional(readOnly = true)
    public Page<BookingDto> getAllBookings(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    /**
     * Get bookings by status with pagination (admin only)
     * @param status Booking status
     * @param pageable Pagination information
     * @return Page of booking DTOs
     */
    @Transactional(readOnly = true)
    public Page<BookingDto> getBookingsByStatus(String status, Pageable pageable) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            return repository.findByStatus(bookingStatus, pageable)
                    .map(this::convertToDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
    
    /**
     * Find all bookings by phone number for non-authenticated users
     *
     * @param phoneNumber the phone number used for booking
     * @return list of booking DTOs
     */
    @Transactional(readOnly = true)
    public List<BookingDto> findBookingsByPhoneNumber(String phoneNumber) {
        log.info("Finding bookings for phone number: {}", phoneNumber);
        List<Booking> bookings = repository.findByPhoneNumberOrderByCreatedAtDesc(phoneNumber);
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Find all bookings by user ID for authenticated users
     *
     * @param userId the user ID
     * @return list of booking DTOs
     */
    @Transactional(readOnly = true)
    public List<BookingDto> findBookingsByUserId(Long userId) {
        log.info("Finding bookings for user ID: {}", userId);
        List<Booking> bookings = repository.findByUserIdOrderByCreatedAtDesc(userId);
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert booking entity to DTO
     * @param booking Booking entity
     * @return Booking DTO
     */
    private BookingDto convertToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        
        dto.setId(booking.getId());
        dto.setTrackingNumber(booking.getTrackingNumber());
        dto.setPhoneNumber(booking.getPhoneNumber());
        
        if (booking.getService() != null) {
            dto.setServiceId(booking.getService().getId());
            dto.setServiceName(booking.getService().getTitle());
        }
        
        dto.setEmail(booking.getEmail());
        dto.setFullName(booking.getFullName());
        dto.setStatus(booking.getStatus().name());
        dto.setNotes(booking.getNotes());
        
        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
        }
        
        if (booking.getCreatedAt() != null) {
            dto.setCreatedAt(booking.getCreatedAt().format(FORMATTER));
        }
        
        if (booking.getUpdatedAt() != null) {
            dto.setUpdatedAt(booking.getUpdatedAt().format(FORMATTER));
        }
        
        // Convert documents to DTOs
        if (booking.getDocuments() != null && !booking.getDocuments().isEmpty()) {
            dto.setDocuments(booking.getDocuments().stream()
                    .map(this::convertToDocumentDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Convert booking document entity to DTO
     * @param document Booking document entity
     * @return Booking document DTO
     */
    private BookingDto.BookingDocumentDto convertToDocumentDto(BookingDocument document) {
        return BookingDto.BookingDocumentDto.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .filePath(document.getFilePath())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .build();
    }
    
    /**
     * Get current authenticated user
     * @return Optional user
     */
    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUsername(userDetails.getUsername());
        }
        
        return Optional.empty();
    }
    
    /**
     * Delete booking by ID
     * @param id Booking ID
     */
    public void deleteBooking(Long id) {
        log.info("Deleting booking with ID: {}", id);
        
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Booking", "id", id);
        }
        
        repository.deleteById(id);
        log.info("Booking with ID: {} deleted successfully", id);
    }
    
    /**
     * Get booking by tracking number
     * @param trackingNumber The tracking number
     * @return Booking DTO
     */
    @Transactional(readOnly = true)
    public BookingDto getBookingByTrackingNumber(String trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "trackingNumber", trackingNumber));
    }
} 