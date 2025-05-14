package com.bensamir.starter.controller.base;

import com.bensamir.starter.controller.mapper.EntityMapper;
import com.bensamir.starter.controller.service.CrudService;
import com.bensamir.starter.exception.ResourceNotFoundException;
import com.bensamir.starter.response.ApiResponse;
import com.bensamir.starter.response.PageResponse;
import com.bensamir.starter.response.ResponseEntityBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Base controller for CRUD operations.
 *
 * @param <E> The entity type
 * @param <D> The DTO type
 * @param <ID> The entity ID type
 */
public abstract class BaseCrudController<E, D, ID> {

    /**
     * Gets the service implementation.
     *
     * @return The service implementation
     */
    protected abstract CrudService<E, ID> getService();

    /**
     * Gets the entity mapper.
     *
     * @return The entity mapper
     */
    protected abstract EntityMapper<E, D> getMapper();

    /**
     * Gets the entity class name, used in error messages.
     *
     * @return The entity class name
     */
    protected abstract String getEntityName();

    /**
     * Creates a new entity.
     *
     * @param dto The DTO containing the entity data
     * @return A response with the created entity
     */
    @PostMapping
    public ResponseEntity<ApiResponse<D>> create(@Valid @RequestBody D dto) {
        E entity = getMapper().toEntity(dto);
        E savedEntity = getService().save(entity);
        D savedDto = getMapper().toDto(savedEntity);
        return ResponseEntityBuilder.created(savedDto);
    }

    /**
     * Updates an existing entity.
     *
     * @param id The entity ID
     * @param dto The DTO containing the updated data
     * @return A response with the updated entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> update(@PathVariable ID id, @Valid @RequestBody D dto) {
        E entity = getService().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), "id", id));

        E updatedEntity = getMapper().updateEntityFromDto(dto, entity);
        E savedEntity = getService().save(updatedEntity);
        D savedDto = getMapper().toDto(savedEntity);

        return ResponseEntityBuilder.success(savedDto);
    }

    /**
     * Gets an entity by its ID.
     *
     * @param id The entity ID
     * @return A response with the entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> findById(@PathVariable ID id) {
        E entity = getService().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), "id", id));

        D dto = getMapper().toDto(entity);
        return ResponseEntityBuilder.success(dto);
    }

    /**
     * Gets all entities.
     *
     * @return A response with a list of entities
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<D>>> findAll() {
        List<E> entities = getService().findAll();
        List<D> dtos = getMapper().toDtoList(entities);
        return ResponseEntityBuilder.success(dtos);
    }

    /**
     * Gets all entities with pagination.
     *
     * @param pageable Pagination information
     * @return A response with a page of entities
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<D>>> findAll(Pageable pageable) {
        Page<E> page = getService().findAll(pageable);
        Page<D> dtoPage = getMapper().toDtoPage(page);
        return ResponseEntityBuilder.page(dtoPage);
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id The entity ID
     * @return A response with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        if (!getService().existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), "id", id);
        }

        getService().deleteById(id);
        return ResponseEntityBuilder.noContent();
    }
}