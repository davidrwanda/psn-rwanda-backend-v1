package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.ReportDto;
import com.psnrwanda.api.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for report operations
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "APIs for generating reports")
public class ReportController {

    private final ReportService reportService;
    
    /**
     * Get dashboard statistics
     * @return Dashboard statistics
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get dashboard statistics", 
            description = "Get dashboard statistics for bookings and services (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ReportDto.DashboardStatsDto> getDashboardStats() {
        ReportDto.DashboardStatsDto stats = reportService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Export bookings
     * @param format Export format (csv or pdf)
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param status Optional status filter
     * @return Exported data
     */
    @GetMapping("/bookings/export")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Export bookings", 
            description = "Export bookings as CSV or PDF (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<?> exportBookings(
            @RequestParam(required = false, defaultValue = "csv") String format,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {
        
        // Get current date if not provided
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ISO_DATE);
        }
        
        if (endDate == null) {
            endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        }
        
        // Create filename with date range
        String filename = "bookings_" + startDate + "_to_" + endDate;
        
        if ("pdf".equalsIgnoreCase(format)) {
            byte[] pdfContent = reportService.exportBookingsAsPdf(startDate, endDate, status);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } else {
            // Default to CSV
            String csvContent = reportService.exportBookingsAsCsv(startDate, endDate, status);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvContent);
        }
    }
} 