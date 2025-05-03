package com.psnrwanda.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Validation error response that extends the standard API error
 * with additional field-level validation details
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError extends ApiError {
    
    private ValidationFieldError[] fieldErrors;
} 