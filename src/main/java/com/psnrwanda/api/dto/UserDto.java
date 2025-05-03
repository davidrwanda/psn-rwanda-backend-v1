package com.psnrwanda.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String fullName;
    
    private String phoneNumber;
    
    private String role;
    
    private boolean enabled;
    
    private LocalDateTime createdAt;
    
    /**
     * DTO for user registration
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationDto {
        
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
        private String username;
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
        
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
        
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;
        
        @Pattern(regexp = "^\\+?[0-9]+$", message = "Invalid phone number format")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        private String phoneNumber;
    }
    
    /**
     * DTO for user registration response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationResponseDto {
        
        private UserDto user;
        
        private String message;
    }
    
    /**
     * DTO for login request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        
        @NotBlank(message = "Username is required")
        private String username;
        
        @NotBlank(message = "Password is required")
        private String password;
    }
    
    /**
     * DTO for login response
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        
        private UserDto user;
        
        private String accessToken;
        
        private String refreshToken;
        
        private String tokenType;
    }
} 