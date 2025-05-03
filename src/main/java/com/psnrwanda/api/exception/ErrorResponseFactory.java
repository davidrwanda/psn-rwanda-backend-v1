package com.psnrwanda.api.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Factory for creating standardized API error responses
 */
public class ErrorResponseFactory {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private ErrorResponseFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Create a generic API error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    
    /**
     * Create a not found error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createNotFoundError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    
    /**
     * Create a bad request error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createBadRequestError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    
    /**
     * Create an authentication error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createAuthError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    
    /**
     * Create a forbidden error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createForbiddenError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
    
    /**
     * Create a conflict error
     *
     * @param message error message
     * @param path request path
     * @return ApiError object
     */
    public static ApiError createConflictError(String message, String path) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
} 