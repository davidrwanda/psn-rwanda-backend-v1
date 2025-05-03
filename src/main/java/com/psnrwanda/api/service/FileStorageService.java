package com.psnrwanda.api.service;

import com.psnrwanda.api.exception.FileStorageException;
import com.psnrwanda.api.model.BookingDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for handling file storage operations
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path fileStorageLocation;
    
    /**
     * Constructor that initializes the file storage location
     * @param uploadDir The upload directory path
     * @throws FileStorageException If unable to create the directory
     */
    public FileStorageService(@Value("${app.file.upload-dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        
        log.info("File storage location set to: {}", this.fileStorageLocation);
    }
    
    /**
     * Store a file and return a BookingDocument entity
     * @param file The file to store
     * @return BookingDocument entity
     */
    public BookingDocument storeFile(MultipartFile file) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        
        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }
            
            // Generate a unique file name to avoid conflicts
            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            // Copy file to the target location (replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Create and return a BookingDocument entity
            return BookingDocument.builder()
                    .fileName(originalFileName)
                    .filePath(uniqueFileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();
                    
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }
    
    /**
     * Retrieve the file path for a stored file
     * @param fileName The file name (path)
     * @return The full path to the file
     */
    public Path getFilePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).normalize();
    }
    
    /**
     * Delete a file from storage
     * @param filePath The file path to delete
     * @return True if the file was deleted successfully
     */
    public boolean deleteFile(String filePath) {
        try {
            Path file = this.fileStorageLocation.resolve(filePath).normalize();
            return Files.deleteIfExists(file);
        } catch (IOException ex) {
            log.error("Error deleting file: {}", filePath, ex);
            return false;
        }
    }
} 