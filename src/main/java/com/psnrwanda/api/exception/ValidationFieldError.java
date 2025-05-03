package com.psnrwanda.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single field validation error
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFieldError {
    
    private String field;
    private String message;
    private String rejectedValue;
} 