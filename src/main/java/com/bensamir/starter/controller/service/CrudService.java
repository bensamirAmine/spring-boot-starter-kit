package com.bensamir.starter.controller.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD service interface.
 *
 * @param <T> The entity type
 * @param <ID> The entity ID type
 */
public interface CrudService<T, ID> {

    /**
     * Saves an entity.
     *
     * @param entity The entity to save
     * @return The saved entity
     */
    T save(T entity);

    /**
     * Finds an entity by its ID.
     *
     * @param id The entity ID
     * @return The entity wrapped in an Optional
     */
    Optional<T> findById(ID id);

    /**
     * Finds all entities.
     *
     * @return A list of all entities
     */
    List<T> findAll();

    /**
     * Finds all entities with pagination.
     *
     * @param pageable Pagination information
     * @return A page of entities
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Deletes an entity by its ID.
     *
     * @param id The entity ID
     */
    void deleteById(ID id);

    /**
     * Checks if an entity exists by its ID.
     *
     * @param id The entity ID
     * @return true if the entity exists, false otherwise
     */
    boolean existsById(ID id);
}