package com.bensamir.starter.controller.mapper;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for mapping between entities and DTOs.
 *
 * @param <E> The entity type
 * @param <D> The DTO type
 */
public interface EntityMapper<E, D> {

    /**
     * Maps an entity to a DTO.
     *
     * @param entity The entity to map
     * @return The mapped DTO
     */
    D toDto(E entity);

    /**
     * Maps a DTO to an entity.
     *
     * @param dto The DTO to map
     * @return The mapped entity
     */
    E toEntity(D dto);

    /**
     * Maps a list of entities to a list of DTOs.
     *
     * @param entities The entities to map
     * @return The mapped DTOs
     */
    List<D> toDtoList(List<E> entities);

    /**
     * Maps a page of entities to a page of DTOs.
     *
     * @param page The page of entities to map
     * @return The mapped page of DTOs
     */
    default Page<D> toDtoPage(Page<E> page) {
        return page.map(this::toDto);
    }

    /**
     * Updates an entity from a DTO.
     *
     * @param dto The DTO containing the updated data
     * @param entity The entity to update
     * @return The updated entity
     */
    E updateEntityFromDto(D dto, E entity);
}