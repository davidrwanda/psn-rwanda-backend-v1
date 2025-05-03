package com.psnrwanda.api.service.common;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD service interface
 * @param <T> Entity type
 * @param <ID> ID type
 */
public interface CrudService<T, ID> {
    
    /**
     * Create a new entity
     * @param entity Entity to create
     * @return Created entity
     */
    T create(T entity);
    
    /**
     * Find all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Find entity by ID
     * @param id Entity ID
     * @return Entity if found
     */
    Optional<T> findById(ID id);
    
    /**
     * Update an existing entity
     * @param id Entity ID
     * @param entity Updated entity data
     * @return Updated entity
     */
    T update(ID id, T entity);
    
    /**
     * Delete an entity by ID
     * @param id Entity ID
     */
    void delete(ID id);
    
    /**
     * Check if entity exists by ID
     * @param id Entity ID
     * @return True if entity exists
     */
    boolean existsById(ID id);
} 