package com.psnrwanda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paginated response for API endpoints
 * @param <T> Type of data in the response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    
    /**
     * Content of the current page
     */
    private List<T> data;
    
    /**
     * Current page number (0-based)
     */
    private int currentPage;
    
    /**
     * Total number of items across all pages
     */
    private long totalItems;
    
    /**
     * Total number of pages
     */
    private int totalPages;
}