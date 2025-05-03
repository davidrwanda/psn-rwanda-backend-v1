package com.psnrwanda.api.controller;

import com.psnrwanda.api.dto.BookingDto;
import com.psnrwanda.api.dto.NotificationDto;
import com.psnrwanda.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jakarta.mail.internet.MimeMessage;

/**
 * Controller for testing email notifications
 */
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Tag(name = "Test", description = "Endpoints for testing features")
@Slf4j
public class TestEmailController {

    private final NotificationService notificationService;
    private final Environment environment;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${spring.mail.host:unknown}")
    private String mailHost;
    
    @Value("${spring.mail.port:0}")
    private String mailPort;
    
    @Value("${spring.mail.username:unknown}")
    private String mailUsername;
    
    @GetMapping("/email/configuration")
    @Operation(summary = "View current email configuration", description = "Displays the current email configuration (without password)")
    public ResponseEntity<Map<String, Object>> getEmailConfiguration() {
        log.info("Getting email configuration details");
        
        Map<String, Object> config = new HashMap<>();
        config.put("mailHost", mailHost);
        config.put("mailPort", mailPort);
        config.put("mailUsername", mailUsername);
        config.put("fromEmail", notificationService.getFromEmail());
        config.put("activeProfiles", Arrays.asList(environment.getActiveProfiles()));
        config.put("timestamp", LocalDateTime.now().format(FORMATTER));
        
        // Include mail configuration properties from the environment
        Map<String, String> mailProperties = new HashMap<>();
        String[] properties = {
            "spring.mail.properties.mail.smtp.auth",
            "spring.mail.properties.mail.smtp.starttls.enable",
            "spring.mail.properties.mail.smtp.ssl.enable",
            "spring.mail.properties.mail.debug",
            "spring.mail.properties.mail.smtp.debug",
            "spring.mail.properties.mail.transport.protocol",
            "spring.mail.properties.mail.test-connection"
        };
        
        for (String prop : properties) {
            String value = environment.getProperty(prop);
            if (value != null) {
                mailProperties.put(prop.substring("spring.mail.properties.".length()), value);
            }
        }
        config.put("mailProperties", mailProperties);
        
        return ResponseEntity.ok(config);
    }

    @GetMapping("/email/confirmation")
    @Operation(summary = "Test booking confirmation email", description = "Sends a test booking confirmation email to specified email address")
    public ResponseEntity<String> testConfirmationEmail(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending test booking confirmation email to: {}", recipient);
        
        // Create a sample booking DTO
        BookingDto booking = createSampleBooking();
        
        // Send the email
        try {
            NotificationDto.NotificationResponseDto response = 
                    notificationService.sendBookingConfirmation(booking, recipient);
            
            return ResponseEntity.ok("Email sending result: " + response.isSuccess() + 
                                    " - " + response.getMessage() + 
                                    " - To: " + recipient + 
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending confirmation email", e);
            return ResponseEntity.ok("Failed to send email: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    @GetMapping("/email/status-update")
    @Operation(summary = "Test booking status update email", description = "Sends a test booking status update email to specified email address")
    public ResponseEntity<String> testStatusUpdateEmail(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending test booking status update email to: {}", recipient);
        
        // Create a sample booking DTO
        BookingDto booking = createSampleBooking();
        booking.setStatus("APPROVED");
        
        // Send the email
        try {
            NotificationDto.NotificationResponseDto response = 
                    notificationService.sendBookingStatusUpdate(booking, recipient, 
                            "Your appointment has been scheduled for tomorrow at 10:00 AM.");
            
            return ResponseEntity.ok("Email sending result: " + response.isSuccess() + 
                                    " - " + response.getMessage() + 
                                    " - To: " + recipient + 
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending status update email", e);
            return ResponseEntity.ok("Failed to send email: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    @GetMapping("/email/admin-notification")
    @Operation(summary = "Test admin notification email", description = "Sends a test admin notification email to specified email address")
    public ResponseEntity<String> testAdminNotificationEmail(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending test admin notification email to: {}", recipient);
        
        // Create a sample booking DTO
        BookingDto booking = createSampleBooking();
        
        // Send the email
        try {
            NotificationDto.NotificationResponseDto response = 
                    notificationService.sendAdminBookingNotification(booking, recipient);
            
            return ResponseEntity.ok("Email sending result: " + response.isSuccess() + 
                                    " - " + response.getMessage() + 
                                    " - To: " + recipient + 
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending admin notification email", e);
            return ResponseEntity.ok("Failed to send email: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    @GetMapping("/email/simple")
    @Operation(summary = "Test simple plain text email", description = "Sends a simple plain text email to the specified email address")
    public ResponseEntity<String> testSimpleEmail(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending simple plain text email for debugging to: {}", recipient);
        
        try {
            NotificationDto.EmailNotificationDto emailDto = NotificationDto.EmailNotificationDto.builder()
                    .email(recipient)
                    .subject("PSN RWANDA - Test Email")
                    .message("This is a simple test email from PSN Rwanda system. No templates, just plain text.\n\n" +
                             "Timestamp: " + LocalDateTime.now().format(FORMATTER) + "\n" +
                             "Mail configuration: " + mailHost + ":" + mailPort + " (" + mailUsername + ")\n" +
                             "Active profiles: " + Arrays.toString(environment.getActiveProfiles()) + "\n\n" +
                             "If you're receiving this, the email delivery is working!")
                    .build();
            
            // Send the email
            NotificationDto.NotificationResponseDto response = notificationService.sendEmail(emailDto);
            
            return ResponseEntity.ok("Email sending result: " + response.isSuccess() + 
                                    " - " + response.getMessage() + 
                                    " - To: " + recipient +
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending simple email", e);
            return ResponseEntity.ok("Failed to send email: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    @GetMapping("/email/test-with-attachment")
    @Operation(summary = "Test email with attachment", description = "Sends a test email with a text file attachment")
    public ResponseEntity<String> testEmailWithAttachment(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending test email with attachment to: {}", recipient);
        
        try {
            // Create a temporary text file for testing
            String filename = "test-email-attachment.txt";
            String content = "This is a test file attached to verify email delivery capabilities.\n" +
                            "Generated at: " + LocalDateTime.now().format(FORMATTER) + "\n" +
                            "Mail configuration: " + mailHost + ":" + mailPort + " (" + mailUsername + ")\n" +
                            "Active profiles: " + Arrays.toString(environment.getActiveProfiles()) + "\n\n" +
                            "For debugging email delivery issues from PSN Rwanda system.";
            
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("email-test-", ".txt");
            java.nio.file.Files.writeString(tempFile, content);
            
            // Send email with the attachment
            MimeMessage mimeMessage = notificationService.getJavaMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            
            helper.setFrom(notificationService.getFormattedFromAddress());
            helper.setTo(recipient);
            helper.setSubject("PSN RWANDA - Test Email with Attachment");
            helper.setText("This email contains a test attachment. If you can see both this message and the attachment, " +
                          "then email delivery is working correctly.\n\n" +
                          "Timestamp: " + LocalDateTime.now().format(FORMATTER) + "\n" +
                          "Mail configuration: " + mailHost + ":" + mailPort + " (" + mailUsername + ")\n" +
                          "Active profiles: " + Arrays.toString(environment.getActiveProfiles()));
            
            helper.addAttachment(filename, tempFile.toFile());
            
            notificationService.getJavaMailSender().send(mimeMessage);
            
            // Delete the temporary file
            java.nio.file.Files.deleteIfExists(tempFile);
            
            return ResponseEntity.ok("Test email with attachment sent to: " + recipient + 
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending test email with attachment", e);
            return ResponseEntity.ok("Failed to send email with attachment: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    @GetMapping("/email/booking-with-provider")
    @Operation(summary = "Test booking notification with provider copy", description = "Sends a test booking confirmation email to client and provider")
    public ResponseEntity<String> testBookingWithProviderNotification(
            @RequestParam(value = "to", defaultValue = "daveleo250@gmail.com") String recipient) {
        log.info("Sending test booking confirmation email to client and provider: {}", recipient);
        
        // Create a sample booking DTO
        BookingDto booking = createSampleBooking();
        
        // Send the email
        try {
            NotificationDto.NotificationResponseDto response = 
                    notificationService.sendBookingConfirmationWithProviderNotification(booking, recipient);
            
            return ResponseEntity.ok("Email sending result: " + response.isSuccess() + 
                                    " - " + response.getMessage() + 
                                    " - To client: " + recipient + 
                                    " - To provider: " + notificationService.getProviderEmail() +
                                    " - Using mail server: " + mailHost + ":" + mailPort);
        } catch (Exception e) {
            log.error("Error sending notifications", e);
            return ResponseEntity.ok("Failed to send emails: " + e.getMessage() + " - Check server logs for details");
        }
    }
    
    private BookingDto createSampleBooking() {
        // Create a sample booking for testing
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setTrackingNumber("PSN-001");
        booking.setPhoneNumber("+250788123456");
        booking.setEmail("client@example.com");
        booking.setFullName("John Doe");
        booking.setServiceId(1L);
        booking.setServiceName("Notary Services");
        booking.setStatus("PENDING");
        booking.setNotes("This is a test booking for email template demonstration");
        booking.setCreatedAt(LocalDateTime.now().format(FORMATTER));
        booking.setUpdatedAt(LocalDateTime.now().format(FORMATTER));
        
        // Add some sample documents
        List<BookingDto.BookingDocumentDto> documents = new ArrayList<>();
        
        BookingDto.BookingDocumentDto doc1 = new BookingDto.BookingDocumentDto();
        doc1.setId(1L);
        doc1.setFileName("contract.pdf");
        doc1.setFileType("application/pdf");
        doc1.setFileSize(1024000L);
        
        BookingDto.BookingDocumentDto doc2 = new BookingDto.BookingDocumentDto();
        doc2.setId(2L);
        doc2.setFileName("identity.jpg");
        doc2.setFileType("image/jpeg");
        doc2.setFileSize(250000L);
        
        documents.add(doc1);
        documents.add(doc2);
        
        booking.setDocuments(documents);
        
        return booking;
    }
} 