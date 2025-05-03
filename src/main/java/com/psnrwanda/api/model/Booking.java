package com.psnrwanda.api.model;

import com.psnrwanda.api.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Booking entity for storing booking information
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {

    public enum BookingStatus {
        PENDING,
        APPROVED,
        IN_PROGRESS,
        COMPLETED,
        REJECTED,
        CANCELLED
    }

    @Column(name = "tracking_number", unique = true, length = 20)
    private String trackingNumber;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(name = "email", length = 100, nullable = true)
    private String email;
    
    @Column(name = "full_name", length = 100, nullable = true)
    private String fullName;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    /**
     * The service that is being booked
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    /**
     * The user who booked the service (can be null for anonymous bookings)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    /**
     * Documents uploaded for this booking
     */
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDocument> documents = new ArrayList<>();
    
    /**
     * Add a document to this booking
     * @param document The document to add
     */
    public void addDocument(BookingDocument document) {
        documents.add(document);
        document.setBooking(this);
    }
    
    /**
     * Remove a document from this booking
     * @param document The document to remove
     */
    public void removeDocument(BookingDocument document) {
        documents.remove(document);
        document.setBooking(null);
    }
} 