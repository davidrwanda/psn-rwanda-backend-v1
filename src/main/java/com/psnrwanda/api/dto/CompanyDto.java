package com.psnrwanda.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Company entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    
    private Long id;
    
    @NotBlank(message = "Company name is required")
    private String name;
    
    private String description;
    
    private String address;
    
    private String phone;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String companyCode;
    
    private String incorporatedOn;
    
    private String website;
    
    private String logoUrl;
    
    private String createdAt;
    
    private String updatedAt;
} 