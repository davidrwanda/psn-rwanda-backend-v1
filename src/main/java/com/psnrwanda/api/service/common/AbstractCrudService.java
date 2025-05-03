package com.psnrwanda.api.service.common;

import com.psnrwanda.api.exception.ResourceNotFoundException;
import com.psnrwanda.api.model.common.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base implementation of CRUD service using Template Method pattern
 * @param <T> Entity type extending BaseEntity
 * @param <R> Repository type extending JpaRepository
 */
public abstract class AbstractCrudService<T extends BaseEntity, R extends JpaRepository<T, Long>> 
        implements CrudService<T, Long> {
    
    protected final R repository;
    protected final String entityName;
    
    /**
     * Constructor with repository and entity name
     * @param repository JPA repository
     * @param entityName Name of the entity for error messages
     */
    protected AbstractCrudService(R repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }
    
    /**
     * Template method to prepare entity before create
     * Can be overridden by subclasses to add custom logic
     * @param entity Entity to prepare
     */
    protected void prepareForCreate(T entity) {
        // Default implementation does nothing - hook for subclasses
    }
    
    /**
     * Template method to prepare entity before update
     * Can be overridden by subclasses to add custom logic
     * @param existingEntity Existing entity from database
     * @param newEntity New entity data from request
     */
    protected void prepareForUpdate(T existingEntity, T newEntity) {
        // Default implementation does nothing - hook for subclasses
    }

    @Override
    public T create(T entity) {
        prepareForCreate(entity);
        return repository.save(entity);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public T update(Long id, T entity) {
        return repository.findById(id)
                .map(existingEntity -> {
                    prepareForUpdate(existingEntity, entity);
                    return repository.save(existingEntity);
                })
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "id", id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(entityName, "id", id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
} 