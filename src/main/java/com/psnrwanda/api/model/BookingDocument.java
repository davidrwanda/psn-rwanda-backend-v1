package com.psnrwanda.api.model;

import com.psnrwanda.api.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity for storing documents related to bookings
 */
@Entity
@Table(name = "booking_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDocument extends BaseEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    /**
     * The booking that this document belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
} 