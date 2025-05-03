package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * REST controller for managing booking documents
 */
@RestController
@RequestMapping("/api/v1/bookings/documents")
@Tag(name = "Booking Documents", description = "API for managing booking documents")
@RequiredArgsConstructor
@Slf4j
public class BookingDocumentController {

    private final BookingService bookingService;
    
    /**
     * Upload documents for a booking
     * 
     * @param files The files to upload
     * @return List of booking document DTOs
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload documents for a booking", description = "Upload one or more documents and get document IDs to use with booking creation")
    public ResponseEntity<List<BookingDto.BookingDocumentDto>> uploadDocuments(
            @RequestParam("files") List<MultipartFile> files) {
        
        if (files == null || files.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        log.info("Received request to upload {} documents", files.size());
        List<BookingDto.BookingDocumentDto> uploadedDocuments = bookingService.uploadDocuments(files);
        
        return ResponseEntity.ok(uploadedDocuments);
    }
} 