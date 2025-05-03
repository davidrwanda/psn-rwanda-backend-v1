package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.NotificationDto;
import com.psnrwanda.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for notification operations
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "APIs for sending notifications")
public class NotificationController {

    private final NotificationService notificationService;
    
    /**
     * Send SMS notification
     * @param smsDto SMS notification DTO
     * @return Notification response
     */
    @PostMapping("/sms")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Send SMS notification", 
            description = "Send SMS notification to client (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<NotificationDto.NotificationResponseDto> sendSms(
            @Valid @RequestBody NotificationDto.SmsNotificationDto smsDto) {
        NotificationDto.NotificationResponseDto response = notificationService.sendSms(smsDto);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Send email notification
     * @param emailDto Email notification DTO
     * @return Notification response
     */
    @PostMapping("/email")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Send email notification", 
            description = "Send email notification to client or admin (admin only)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<NotificationDto.NotificationResponseDto> sendEmail(
            @Valid @RequestBody NotificationDto.EmailNotificationDto emailDto) {
        NotificationDto.NotificationResponseDto response = notificationService.sendEmail(emailDto);
        return ResponseEntity.ok(response);
    }
} 