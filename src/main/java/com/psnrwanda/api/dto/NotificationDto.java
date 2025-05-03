package com.psnrwanda.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Base class for notification DTOs
 */
public class NotificationDto {

    /**
     * DTO for SMS notifications
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmsNotificationDto {
        
        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
        private String phoneNumber;
        
        @NotBlank(message = "Message is required")
        @Size(max = 160, message = "SMS message must not exceed 160 characters")
        private String message;
    }
    
    /**
     * DTO for email notifications
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailNotificationDto {
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
        
        @NotBlank(message = "Subject is required")
        @Size(max = 100, message = "Subject must not exceed 100 characters")
        private String subject;
        
        private String message;
        
        /**
         * Optional HTML template name (without extension)
         * If provided, the message field will be ignored and the template will be used
         */
        private String template;
        
        /**
         * Variables to be used in the HTML template
         */
        private Map<String, Object> templateVariables;
    }
    
    /**
     * DTO for notification responses
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponseDto {
        
        private boolean success;
        
        private String message;
        
        private String timestamp;
    }
} 