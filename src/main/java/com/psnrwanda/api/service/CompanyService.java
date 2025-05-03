package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.CompanyDto;
import com.psnrwanda.api.exception.ResourceNotFoundException;
import com.psnrwanda.api.model.Company;
import com.psnrwanda.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for managing company information
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final CompanyRepository companyRepository;
    
    /**
     * Get company information
     * Since there should only be one company record, we'll get the first one or create a default if none exists
     * 
     * @return Company DTO
     */
    @Transactional(readOnly = true)
    public CompanyDto getCompanyInfo() {
        List<Company> companies = companyRepository.findAll();
        
        if (companies.isEmpty()) {
            // Create a default company record
            return createDefaultCompany();
        }
        
        return convertToDto(companies.get(0));
    }
    
    /**
     * Update company information
     * 
     * @param companyDto Company DTO with updated information
     * @return Updated company DTO
     */
    @Transactional
    public CompanyDto updateCompanyInfo(CompanyDto companyDto) {
        List<Company> companies = companyRepository.findAll();
        
        Company company;
        if (companies.isEmpty()) {
            company = new Company();
        } else {
            company = companies.get(0);
        }
        
        updateEntityFromDto(company, companyDto);
        
        Company savedCompany = companyRepository.save(company);
        return convertToDto(savedCompany);
    }
    
    /**
     * Create a default company record if none exists
     * 
     * @return Default company DTO
     */
    @Transactional
    private CompanyDto createDefaultCompany() {
        Company company = new Company();
        company.setName("PSN RWANDA Ltd");
        company.setDescription("Legal Activities (Notary Services), Consultancy, Leasing, Real Estate, R&D");
        company.setAddress("Nyamabuye, Muhanga, Southern Province, Rwanda");
        company.setPhone("+250 788 859 612");
        company.setEmail("info@psnrwanda.com");
        company.setCompanyCode("121058604");
        company.setIncorporatedOn(LocalDate.of(2023, 1, 23));
        
        Company savedCompany = companyRepository.save(company);
        return convertToDto(savedCompany);
    }
    
    /**
     * Update entity from DTO
     * 
     * @param company Company entity to update
     * @param companyDto DTO with updated values
     */
    private void updateEntityFromDto(Company company, CompanyDto companyDto) {
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setAddress(companyDto.getAddress());
        company.setPhone(companyDto.getPhone());
        company.setEmail(companyDto.getEmail());
        company.setCompanyCode(companyDto.getCompanyCode());
        company.setWebsite(companyDto.getWebsite());
        company.setLogoUrl(companyDto.getLogoUrl());
        
        if (companyDto.getIncorporatedOn() != null && !companyDto.getIncorporatedOn().isEmpty()) {
            company.setIncorporatedOn(LocalDate.parse(companyDto.getIncorporatedOn(), DATE_FORMATTER));
        }
    }
    
    /**
     * Convert entity to DTO
     * 
     * @param company Company entity
     * @return Company DTO
     */
    private CompanyDto convertToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setAddress(company.getAddress());
        dto.setPhone(company.getPhone());
        dto.setEmail(company.getEmail());
        dto.setCompanyCode(company.getCompanyCode());
        dto.setWebsite(company.getWebsite());
        dto.setLogoUrl(company.getLogoUrl());
        
        if (company.getIncorporatedOn() != null) {
            dto.setIncorporatedOn(company.getIncorporatedOn().format(DATE_FORMATTER));
        }
        
        if (company.getCreatedAt() != null) {
            dto.setCreatedAt(company.getCreatedAt().format(DATETIME_FORMATTER));
        }
        
        if (company.getUpdatedAt() != null) {
            dto.setUpdatedAt(company.getUpdatedAt().format(DATETIME_FORMATTER));
        }
        
        return dto;
    }
} 