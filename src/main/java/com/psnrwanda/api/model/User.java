package com.psnrwanda.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psnrwanda.api.model.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * User entity for authentication and user management
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "username", unique = true)
    private String username;
    
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", unique = true)
    private String email;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "password")
    @JsonIgnore
    private String password;
    
    @Size(max = 100)
    @Column(name = "full_name")
    private String fullName;
    
    @Size(max = 20)
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "enabled")
    private boolean enabled = true;
    
    @NotBlank
    @Size(max = 20)
    @Column(name = "role")
    private String role = "ROLE_USER";
} 