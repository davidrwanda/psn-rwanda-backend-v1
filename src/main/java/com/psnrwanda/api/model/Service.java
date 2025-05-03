package com.psnrwanda.api.model;

import com.psnrwanda.api.model.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service entity for notary and professional services
 */
@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service extends BaseEntity {
    
    @NotBlank(message = "Title is required")
    @Size(max = 100)
    @Column(name = "title")
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ElementCollection
    @CollectionTable(
        name = "service_bullet_points",
        joinColumns = @JoinColumn(name = "service_id")
    )
    @Column(name = "bullet_point", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> bulletPoints = new ArrayList<>();
    
    @Column(name = "turnaround_time")
    private String turnaroundTime;
    
    @Column(name = "cta_text")
    private String ctaText;
    
    @Column(name = "price_info")
    private String priceInfo;
    
    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;
    
    @OneToMany(mappedBy = "service")
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
} 