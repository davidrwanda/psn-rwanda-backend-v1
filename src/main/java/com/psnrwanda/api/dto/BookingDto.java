package com.psnrwanda.api.dto;

import com.psnrwanda.api.model.Booking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Booking entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    
    private Long id;
    
    private String trackingNumber;
    
    private String phoneNumber;
    
    private Long serviceId;
    
    private String serviceName;
    
    private String email;
    
    private String fullName;
    
    private String notes;
    
    private String status;
    
    private Long userId;
    
    private String createdAt;
    
    private String updatedAt;
    
    @Builder.Default
    private List<BookingDocumentDto> documents = new ArrayList<>();
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDocumentDto {
        private Long id;
        private String fileName;
        private String filePath;
        private String fileType;
        private Long fileSize;
    }
    
    /**
     * DTO for creating a booking
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBookingDto {
        
        @NotNull(message = "Service ID is required")
        private Long serviceId;
        
        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
        private String phoneNumber;
        
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        @Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
                message = "Invalid email format")
        private String email;
        
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;
        
        private String notes;
        
        @Builder.Default
        private List<String> documentIds = new ArrayList<>();
    }
    
    /**
     * DTO for updating booking status
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBookingStatusDto {
        
        @NotBlank(message = "Status is required")
        private String status;
        
        private String notes;
    }
    
    /**
     * DTO for booking response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingResponseDto {
        
        private BookingDto booking;
        
        private String message;
    }
    
    /**
     * DTO for booking tracking request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrackBookingDto {
        
        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
        private String phoneNumber;
    }
} 