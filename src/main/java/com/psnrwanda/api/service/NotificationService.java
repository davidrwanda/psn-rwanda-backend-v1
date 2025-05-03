package com.psnrwanda.api.service;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.dto.NotificationDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for sending notifications
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Value("${spring.mail.enabled:false}")
    private boolean mailEnabled;
    
    @Value("${notification.sms.enabled:false}")
    private boolean smsEnabled;
    
    @Value("${spring.mail.username:info@psnrwanda.com}")
    private String fromEmail;
    
    @Value("${app.url:http://localhost:8082}")
    private String appUrl;
    
    @Value("${app.notification.provider-email:info@oneclic.vet}")
    private String providerEmail;
    
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    
    /**
     * Send SMS notification
     * 
     * @param smsDto SMS notification DTO
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendSms(NotificationDto.SmsNotificationDto smsDto) {
        log.info("Sending SMS to {}", smsDto.getPhoneNumber());
        
        if (!smsEnabled) {
            log.warn("SMS notifications are disabled. Message to {} was not sent.", smsDto.getPhoneNumber());
            return createResponse(false, "SMS notifications are currently disabled", LocalDateTime.now());
        }
        
        try {
            // Integration with SMS service would go here
            // For now, we'll just log the message
            log.info("SMS Content: {}", smsDto.getMessage());
            
            // Simulate SMS sending success
            return createResponse(true, "SMS sent successfully", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", smsDto.getPhoneNumber(), e.getMessage());
            return createResponse(false, "Failed to send SMS: " + e.getMessage(), LocalDateTime.now());
        }
    }
    
    /**
     * Send email notification using HTML template
     * 
     * @param emailDto Email notification DTO
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendEmail(NotificationDto.EmailNotificationDto emailDto) {
        log.info("Sending email to {}", emailDto.getEmail());
        
        if (!mailEnabled) {
            log.warn("Email notifications are disabled. Message to {} was not sent.", emailDto.getEmail());
            return createResponse(false, "Email notifications are currently disabled", LocalDateTime.now());
        }
        
        try {
            // If template is specified, use it, otherwise send plain text email
            if (emailDto.getTemplate() != null && !emailDto.getTemplate().isEmpty()) {
                return sendTemplatedEmail(emailDto.getEmail(), emailDto.getSubject(), 
                        emailDto.getTemplate(), emailDto.getTemplateVariables());
            } else {
                // Fall back to plain text email
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(getFormattedFromAddress());
                message.setTo(emailDto.getEmail());
                message.setSubject(emailDto.getSubject());
                message.setText(emailDto.getMessage());
                
                emailSender.send(message);
                
                return createResponse(true, "Email sent successfully", LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", emailDto.getEmail(), e.getMessage());
            return createResponse(false, "Failed to send email: " + e.getMessage(), LocalDateTime.now());
        }
    }
    
    /**
     * Send HTML email using a Thymeleaf template
     * 
     * @param to Recipient email
     * @param subject Email subject
     * @param templateName Template name (without extension)
     * @param variables Template variables
     * @return Notification response DTO
     */
    private NotificationDto.NotificationResponseDto sendTemplatedEmail(
            String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(getFormattedFromAddress());
            helper.setTo(to);
            helper.setSubject(subject);
            
            // Process template
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            
            // Add logo as inline image
            helper.addInline("company-logo", new ClassPathResource("static/images/logo.png"));
            
            String htmlContent = templateEngine.process("email/" + templateName, context);
            helper.setText(htmlContent, true);
            
            emailSender.send(mimeMessage);
            
            return createResponse(true, "Email sent successfully", LocalDateTime.now());
        } catch (MessagingException e) {
            log.error("Failed to send templated email to {}: {}", to, e.getMessage());
            return createResponse(false, "Failed to send email: " + e.getMessage(), LocalDateTime.now());
        }
    }
    
    /**
     * Send booking confirmation email to client
     * 
     * @param booking Booking DTO
     * @param email Client email
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendBookingConfirmation(BookingDto booking, String email) {
        if (email == null || email.isEmpty()) {
            log.warn("Cannot send booking confirmation: email is missing");
            return createResponse(false, "Email address is missing", LocalDateTime.now());
        }
        
        String clientName = booking.getFullName() != null && !booking.getFullName().isEmpty() 
                ? booking.getFullName() : "Client";
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("clientName", clientName);
        variables.put("trackingNumber", booking.getTrackingNumber());
        variables.put("serviceName", booking.getServiceName());
        variables.put("bookingDate", booking.getCreatedAt());
        variables.put("bookingStatus", booking.getStatus());
        variables.put("documents", booking.getDocuments());
        variables.put("notes", booking.getNotes());
        variables.put("trackingUrl", appUrl + "/booking/track?number=" + booking.getTrackingNumber());
        
        NotificationDto.EmailNotificationDto emailDto = NotificationDto.EmailNotificationDto.builder()
                .email(email)
                .subject("PSN RWANDA - Booking Confirmation")
                .template("booking-confirmation")
                .templateVariables(variables)
                .build();
        
        return sendEmail(emailDto);
    }
    
    /**
     * Send booking status update email to client
     * 
     * @param booking Booking DTO
     * @param email Client email
     * @param statusMessage Additional status message
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendBookingStatusUpdate(
            BookingDto booking, String email, String statusMessage) {
        if (email == null || email.isEmpty()) {
            log.warn("Cannot send booking status update: email is missing");
            return createResponse(false, "Email address is missing", LocalDateTime.now());
        }
        
        String clientName = booking.getFullName() != null && !booking.getFullName().isEmpty() 
                ? booking.getFullName() : "Client";
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("clientName", clientName);
        variables.put("trackingNumber", booking.getTrackingNumber());
        variables.put("serviceName", booking.getServiceName());
        variables.put("bookingStatus", booking.getStatus());
        variables.put("updatedDate", booking.getUpdatedAt());
        variables.put("notes", booking.getNotes());
        variables.put("statusMessage", statusMessage);
        variables.put("trackingUrl", appUrl + "/booking/track?number=" + booking.getTrackingNumber());
        
        NotificationDto.EmailNotificationDto emailDto = NotificationDto.EmailNotificationDto.builder()
                .email(email)
                .subject("PSN RWANDA - Booking Status Update")
                .template("booking-status-update")
                .templateVariables(variables)
                .build();
        
        return sendEmail(emailDto);
    }
    
    /**
     * Send new booking notification to admin
     * 
     * @param booking Booking DTO
     * @param adminEmail Admin email
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendAdminBookingNotification(
            BookingDto booking, String adminEmail) {
        if (adminEmail == null || adminEmail.isEmpty()) {
            log.warn("Cannot send admin notification: email is missing");
            return createResponse(false, "Admin email address is missing", LocalDateTime.now());
        }
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("bookingId", booking.getId());
        variables.put("trackingNumber", booking.getTrackingNumber());
        variables.put("serviceName", booking.getServiceName());
        variables.put("bookingStatus", booking.getStatus());
        variables.put("createdDate", booking.getCreatedAt());
        variables.put("notes", booking.getNotes());
        variables.put("phoneNumber", booking.getPhoneNumber());
        variables.put("email", booking.getEmail());
        variables.put("fullName", booking.getFullName());
        variables.put("userId", booking.getUserId());
        variables.put("documents", booking.getDocuments());
        variables.put("adminUrl", appUrl + "/admin/bookings/" + booking.getId());
        
        NotificationDto.EmailNotificationDto emailDto = NotificationDto.EmailNotificationDto.builder()
                .email(adminEmail)
                .subject("PSN RWANDA - New Booking Notification")
                .template("admin-booking-notification")
                .templateVariables(variables)
                .build();
        
        return sendEmail(emailDto);
    }
    
    /**
     * Send booking confirmation email to both client and provider
     * 
     * @param booking Booking DTO
     * @param clientEmail Client email
     * @return Notification response DTO
     */
    public NotificationDto.NotificationResponseDto sendBookingConfirmationWithProviderNotification(BookingDto booking, String clientEmail) {
        // First send confirmation to client
        NotificationDto.NotificationResponseDto clientResponse = sendBookingConfirmation(booking, clientEmail);
        
        // Then send notification to provider
        sendAdminBookingNotification(booking, providerEmail);
        
        // Return the client response
        return clientResponse;
    }
    
    /**
     * Create notification response
     * 
     * @param success Whether the notification was successful
     * @param message Response message
     * @param timestamp Timestamp
     * @return Notification response DTO
     */
    private NotificationDto.NotificationResponseDto createResponse(boolean success, String message, LocalDateTime timestamp) {
        return NotificationDto.NotificationResponseDto.builder()
                .success(success)
                .message(message)
                .timestamp(timestamp.format(FORMATTER))
                .build();
    }
    
    /**
     * Get the JavaMailSender for direct use
     * @return JavaMailSender instance
     */
    public JavaMailSender getJavaMailSender() {
        return emailSender;
    }
    
    /**
     * Get the from email address
     * @return From email address
     */
    public String getFromEmail() {
        return fromEmail;
    }
    
    /**
     * Get properly formatted sender address with display name
     * @return Formatted sender address with display name
     */
    public String getFormattedFromAddress() {
        try {
            return "PSN RWANDA <" + fromEmail + ">";
        } catch (Exception e) {
            log.warn("Error formatting sender address, using plain email: {}", e.getMessage());
            return fromEmail;
        }
    }
    
    /**
     * Get the provider email address
     * @return Provider email address
     */
    public String getProviderEmail() {
        return providerEmail;
    }
} 