package com.psnrwanda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Base class for report DTOs
 */
public class ReportDto {

    /**
     * DTO for dashboard statistics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStatsDto {
        
        private long totalBookings;
        
        private long pendingBookings;
        
        private long completedBookings;
        
        private long totalServices;
        
        private long activeServices;
        
        private Map<String, Long> bookingsByStatus;
        
        private Map<String, Long> bookingsByService;
        
        private List<BookingTrendDto> bookingTrends;
    }
    
    /**
     * DTO for booking trends over time
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingTrendDto {
        
        private String date;
        
        private long count;
    }
    
    /**
     * DTO for export request parameters
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExportRequestDto {
        
        private String startDate;
        
        private String endDate;
        
        private String status;
        
        private String format;
    }
} 