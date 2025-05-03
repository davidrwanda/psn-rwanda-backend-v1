package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.ServiceDto;
import com.psnrwanda.api.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller for managing services
 */
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Slf4j
public class ServicesController {

    private final ServiceService serviceService;

    /**
     * Get all services
     * @return list of services
     */
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        log.info("Request to get all services");
        return ResponseEntity.ok(serviceService.findAllAsDto());
    }
    
    /**
     * Get active services
     * @return list of active services
     */
    @GetMapping("/active")
    public ResponseEntity<List<ServiceDto>> getActiveServices() {
        log.info("Request to get all active services");
        return ResponseEntity.ok(serviceService.findAllActive());
    }
    
    /**
     * Search services by title
     * @param title title to search for
     * @return list of matching services
     */
    @GetMapping("/search")
    public ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String title) {
        log.info("Request to search services with title: {}", title);
        return ResponseEntity.ok(serviceService.searchByTitle(title));
    }
    
    /**
     * Get service by ID
     * @param id service ID
     * @return service details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        log.info("Request to get service with ID: {}", id);
        return ResponseEntity.ok(serviceService.findByIdAsDto(id));
    }
    
    /**
     * Create a new service (admin only)
     * @param serviceDto service details
     * @return created service
     */
    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        log.info("Request to create new service: {}", serviceDto);
        return ResponseEntity.ok(serviceService.createService(serviceDto));
    }
    
    /**
     * Update a service (admin only)
     * @param id service ID
     * @param serviceDto updated service details
     * @return updated service
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(
            @PathVariable Long id, 
            @RequestBody ServiceDto serviceDto) {
        log.info("Request to update service with ID: {}", id);
        return ResponseEntity.ok(serviceService.updateService(id, serviceDto));
    }
    
    /**
     * Delete a service (admin only)
     * @param id service ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        log.info("Request to delete service with ID: {}", id);
        
        serviceService.delete(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Service deleted successfully");
        response.put("id", id);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Toggle service visibility (admin only)
     * @param id service ID
     * @return updated service
     */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ServiceDto> toggleService(@PathVariable Long id) {
        log.info("Request to toggle visibility of service with ID: {}", id);
        return ResponseEntity.ok(serviceService.toggleServiceStatus(id));
    }
} 