package com.psnrwanda.api.exception;

/**
 * Exception thrown when there's an issue with file storage operations
 */
public class FileStorageException extends RuntimeException {
    
    public FileStorageException(String message) {
        super(message);
    }
    
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
} 