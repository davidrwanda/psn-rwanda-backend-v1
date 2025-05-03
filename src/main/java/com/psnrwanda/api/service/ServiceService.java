package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.ServiceDto;
import com.psnrwanda.api.exception.ResourceNotFoundException;
import com.psnrwanda.api.model.Service;
import com.psnrwanda.api.repository.ServiceRepository;
import com.psnrwanda.api.service.common.AbstractCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Service entity
 */
@Slf4j
@Component
@Transactional
public class ServiceService extends AbstractCrudService<Service, ServiceRepository> {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Constructor
     * @param repository Service repository
     */
    public ServiceService(ServiceRepository repository) {
        super(repository, "Service");
    }
    
    /**
     * Find all active services
     * @return List of active service DTOs
     */
    @Transactional(readOnly = true)
    public List<ServiceDto> findAllActive() {
        return repository.findByActiveTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Search services by title
     * @param title Title to search for
     * @return List of matching service DTOs
     */
    @Transactional(readOnly = true)
    public List<ServiceDto> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all services as DTOs
     * @return List of service DTOs
     */
    @Transactional(readOnly = true)
    public List<ServiceDto> findAllAsDto() {
        return findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Find service by ID and return as DTO
     * @param id Service ID
     * @return Service DTO
     */
    @Transactional(readOnly = true)
    public ServiceDto findByIdAsDto(Long id) {
        return findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
    }
    
    /**
     * Create service from DTO
     * @param serviceDto Service DTO
     * @return Created service DTO
     */
    public ServiceDto createService(ServiceDto serviceDto) {
        Service service = new Service();
        
        service.setTitle(serviceDto.getTitle());
        service.setDescription(serviceDto.getDescription());
        service.setActive(serviceDto.isActive());
        service.setImageUrl(serviceDto.getImageUrl());
        service.setBulletPoints(serviceDto.getBulletPoints());
        service.setTurnaroundTime(serviceDto.getTurnaroundTime());
        service.setCtaText(serviceDto.getCtaText());
        service.setPriceInfo(serviceDto.getPriceInfo());
        service.setAdditionalInfo(serviceDto.getAdditionalInfo());
        
        Service savedService = create(service);
        return convertToDto(savedService);
    }
    
    /**
     * Update service from DTO
     * @param id Service ID
     * @param serviceDto Service DTO
     * @return Updated service DTO
     */
    public ServiceDto updateService(Long id, ServiceDto serviceDto) {
        return findById(id)
                .map(service -> {
                    service.setTitle(serviceDto.getTitle());
                    service.setDescription(serviceDto.getDescription());
                    service.setActive(serviceDto.isActive());
                    service.setImageUrl(serviceDto.getImageUrl());
                    service.setBulletPoints(serviceDto.getBulletPoints());
                    service.setTurnaroundTime(serviceDto.getTurnaroundTime());
                    service.setCtaText(serviceDto.getCtaText());
                    service.setPriceInfo(serviceDto.getPriceInfo());
                    service.setAdditionalInfo(serviceDto.getAdditionalInfo());
                    
                    Service updatedService = update(id, service);
                    return convertToDto(updatedService);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
    }
    
    /**
     * Toggle service active status
     * @param id Service ID
     * @return Updated service DTO
     */
    public ServiceDto toggleServiceStatus(Long id) {
        return findById(id)
                .map(service -> {
                    service.setActive(!service.isActive());
                    Service updatedService = update(id, service);
                    return convertToDto(updatedService);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", id));
    }
    
    /**
     * Convert service entity to DTO
     * @param service Service entity
     * @return Service DTO
     */
    private ServiceDto convertToDto(Service service) {
        ServiceDto dto = new ServiceDto();
        
        dto.setId(service.getId());
        dto.setTitle(service.getTitle());
        dto.setDescription(service.getDescription());
        dto.setActive(service.isActive());
        dto.setImageUrl(service.getImageUrl());
        dto.setBulletPoints(service.getBulletPoints());
        dto.setTurnaroundTime(service.getTurnaroundTime());
        dto.setCtaText(service.getCtaText());
        dto.setPriceInfo(service.getPriceInfo());
        dto.setAdditionalInfo(service.getAdditionalInfo());
        
        if (service.getCreatedAt() != null) {
            dto.setCreatedAt(service.getCreatedAt().format(FORMATTER));
        }
        
        if (service.getUpdatedAt() != null) {
            dto.setUpdatedAt(service.getUpdatedAt().format(FORMATTER));
        }
        
        return dto;
    }
} 