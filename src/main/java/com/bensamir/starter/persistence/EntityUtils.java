package com.bensamir.starter.persistence;

import com.bensamir.starter.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Utility methods for JPA entity operations.
 */
public final class EntityUtils {

    private EntityUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets a reference to an entity without loading it from the database.
     * <p>
     * This is useful for setting relationships without loading the related entity.
     *
     * @param entityManager The EntityManager
     * @param entityClass The entity class
     * @param id The entity ID
     * @param <T> The entity type
     * @return A reference to the entity, or null if the ID is null
     */
    public static <T> T getReference(EntityManager entityManager, Class<T> entityClass, Object id) {
        if (id == null) {
            return null;
        }
        return entityManager.getReference(entityClass, id);
    }

    /**
     * Gets an entity by ID or throws a ResourceNotFoundException if not found.
     * <p>
     * This is useful for loading an entity when you expect it to exist, and want to
     * handle the not-found case consistently.
     *
     * @param repository The repository
     * @param id The entity ID
     * @param entityName The name of the entity (for the error message)
     * @param <T> The entity type
     * @param <ID> The ID type
     * @return The entity
     * @throws ResourceNotFoundException if the entity is not found
     */
    public static <T, ID> T getEntityOrThrow(JpaRepository<T, ID> repository, ID id, String entityName) {
        if (id == null) {
            throw new ResourceNotFoundException(entityName, "id", "null");
        }

        Optional<T> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new ResourceNotFoundException(entityName, "id", id);
        }

        return entity.get();
    }
}