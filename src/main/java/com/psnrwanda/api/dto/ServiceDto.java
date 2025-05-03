package com.psnrwanda.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Service entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    
    private String description;
    
    private boolean active;
    
    private String imageUrl;
    
    @Builder.Default
    private List<String> bulletPoints = new ArrayList<>();
    
    private String turnaroundTime;
    
    private String ctaText;
    
    private String priceInfo;
    
    private String additionalInfo;
    
    private String createdAt;
    private String updatedAt;
} 