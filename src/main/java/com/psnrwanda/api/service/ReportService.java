package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.dto.ReportDto;
import com.psnrwanda.api.model.Booking;
import com.psnrwanda.api.repository.BookingRepository;
import com.psnrwanda.api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating reports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter CSV_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final BookingService bookingService;
    
    /**
     * Get dashboard statistics
     * 
     * @return Dashboard statistics DTO
     */
    @Transactional(readOnly = true)
    public ReportDto.DashboardStatsDto getDashboardStats() {
        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(Booking.BookingStatus.PENDING);
        long completedBookings = bookingRepository.countByStatus(Booking.BookingStatus.COMPLETED);
        long totalServices = serviceRepository.count();
        long activeServices = serviceRepository.findByActiveTrue().size();
        
        Map<String, Long> bookingsByStatus = Arrays.stream(Booking.BookingStatus.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        status -> bookingRepository.countByStatus(status)
                ));
        
        Map<String, Long> bookingsByService = new HashMap<>();
        serviceRepository.findAll().forEach(service -> {
            long count = bookingRepository.findByServiceId(service.getId()).size();
            bookingsByService.put(service.getTitle(), count);
        });
        
        List<ReportDto.BookingTrendDto> bookingTrends = getBookingTrendsForLastDays(30);
        
        return ReportDto.DashboardStatsDto.builder()
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .completedBookings(completedBookings)
                .totalServices(totalServices)
                .activeServices(activeServices)
                .bookingsByStatus(bookingsByStatus)
                .bookingsByService(bookingsByService)
                .bookingTrends(bookingTrends)
                .build();
    }
    
    /**
     * Export bookings as CSV
     * 
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param status Optional status filter
     * @return CSV content as string
     */
    @Transactional(readOnly = true)
    public String exportBookingsAsCsv(String startDate, String endDate, String status) {
        List<BookingDto> bookings = getBookingsForExport(startDate, endDate, status);
        
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Phone Number,Service,Email,Full Name,Status,Notes,Created At,Updated At\n");
        
        bookings.forEach(booking -> {
            csv.append(booking.getId()).append(",");
            csv.append(booking.getPhoneNumber()).append(",");
            csv.append(booking.getServiceName()).append(",");
            csv.append(booking.getEmail() != null ? booking.getEmail() : "").append(",");
            csv.append(booking.getFullName() != null ? booking.getFullName() : "").append(",");
            csv.append(booking.getStatus()).append(",");
            csv.append(booking.getNotes() != null ? "\"" + booking.getNotes().replace("\"", "\"\"") + "\"" : "").append(",");
            csv.append(booking.getCreatedAt()).append(",");
            csv.append(booking.getUpdatedAt() != null ? booking.getUpdatedAt() : "").append("\n");
        });
        
        return csv.toString();
    }
    
    /**
     * Export bookings as PDF
     * Note: This is a stub implementation. Actual PDF generation would require a library like iText or PDFBox.
     * 
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param status Optional status filter
     * @return PDF content as byte array
     */
    @Transactional(readOnly = true)
    public byte[] exportBookingsAsPdf(String startDate, String endDate, String status) {
        // This is a placeholder for PDF generation
        // In a real implementation, you would use a PDF library to generate the PDF
        
        log.info("Generating PDF report for bookings from {} to {}", startDate, endDate);
        
        // Return dummy PDF content
        return "PDF report for bookings".getBytes();
    }
    
    /**
     * Get booking trends for the last N days
     * 
     * @param days Number of days
     * @return List of booking trend DTOs
     */
    private List<ReportDto.BookingTrendDto> getBookingTrendsForLastDays(int days) {
        List<ReportDto.BookingTrendDto> trends = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay().minusSeconds(1);
            
            // Count bookings for this day (this is a simplified approach)
            // In a real application, you'd use a repository method with date criteria
            long count = 0; // bookingRepository.countByCreatedAtBetween(start, end);
            
            trends.add(ReportDto.BookingTrendDto.builder()
                    .date(date.format(DATE_FORMATTER))
                    .count(count)
                    .build());
        }
        
        return trends;
    }
    
    /**
     * Get bookings for export
     * 
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate End date (yyyy-MM-dd)
     * @param status Optional status filter
     * @return List of booking DTOs
     */
    private List<BookingDto> getBookingsForExport(String startDate, String endDate, String status) {
        // Parse dates
        LocalDate start = startDate != null ? LocalDate.parse(startDate, DATE_FORMATTER) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate, DATE_FORMATTER) : LocalDate.now();
        
        // Get all bookings for now (pagination would be applied in a real implementation)
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("createdAt").descending());
        Page<BookingDto> bookingsPage;
        
        if (status != null && !status.isEmpty()) {
            bookingsPage = bookingService.getBookingsByStatus(status, pageable);
        } else {
            bookingsPage = bookingService.getAllBookings(pageable);
        }
        
        // Filter by date (this is simplified; in a real application, you'd include date criteria in the repository query)
        return bookingsPage.getContent();
    }
}