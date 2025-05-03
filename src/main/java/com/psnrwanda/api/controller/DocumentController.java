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
 * REST controller for managing general document uploads without authentication
 */
@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents", description = "API for managing document uploads without authentication")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final BookingService bookingService;
    
    /**
     * Upload documents without requiring authentication
     * 
     * @param files The files to upload
     * @return List of booking document DTOs
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload documents without authentication", description = "Upload one or more documents and get document IDs without requiring authentication")
    public ResponseEntity<List<BookingDto.BookingDocumentDto>> uploadDocuments(
            @RequestParam("files") List<MultipartFile> files) {
        
        if (files == null || files.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        log.info("Received request to upload {} documents via public endpoint", files.size());
        List<BookingDto.BookingDocumentDto> uploadedDocuments = bookingService.uploadDocuments(files);
        
        return ResponseEntity.ok(uploadedDocuments);
    }
} 