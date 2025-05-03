package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.CompanyDto;
import com.psnrwanda.api.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for company information
 */
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Company", description = "Company information APIs")
public class CompanyController {

    private final CompanyService companyService;
    
    /**
     * Get company information
     * @return Company information
     */
    @GetMapping
    @Operation(summary = "Get company information", description = "Retrieve company information")
    public ResponseEntity<CompanyDto> getCompanyInfo() {
        CompanyDto companyDto = companyService.getCompanyInfo();
        return ResponseEntity.ok(companyDto);
    }
    
    /**
     * Update company information (admin only)
     * @param companyDto Company DTO with updated information
     * @return Updated company information
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update company information", 
            description = "Update company information (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<CompanyDto> updateCompanyInfo(@Valid @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompanyInfo(companyDto);
        return ResponseEntity.ok(updatedCompany);
    }
} 