package com.bensamir.starter.persistence;

import jakarta.persistence.EntityManager;

import java.util.Optional;

public final class EntityUtils {

    private EntityUtils() {
        // Utility class, no instantiation
    }

    /**
     * Safely gets a reference to an entity without accessing the database.
     * Useful for setting relationships without loading the related entity.
     */
    public static <T> T getReference(EntityManager entityManager, Class<T> entityClass, Object id) {
        if (id == null) {
            return null;
        }
        return entityManager.getReference(entityClass, id);
    }

    /**
     * Checks if an entity with the given ID exists.
     */
    public static <T, ID> boolean exists(BaseRepository<T, ID> repository, ID id) {
        if (id == null) {
            return false;
        }
        return repository.existsById(id);
    }

    /**
     * Gets an entity by ID and throws a ResourceNotFoundException if not found.
     */
    public static <T, ID> T getEntityOrThrow(BaseRepository<T, ID> repository, ID id, String entityName) {
        if (id == null) {
            throw new com.bensamir.starter.exception.ResourceNotFoundException(entityName, "id", "null");
        }

        Optional<T> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new com.bensamir.starter.exception.ResourceNotFoundException(entityName, "id", id);
        }

        return entity.get();
    }
}