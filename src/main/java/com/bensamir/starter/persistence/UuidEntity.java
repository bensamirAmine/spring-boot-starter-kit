package com.bensamir.starter.persistence;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;
import java.util.UUID;

/**
 * Base entity with UUID primary key.
 * <p>
 * This class provides:
 * <ul>
 *   <li>Auto-generated UUID ID field</li>
 *   <li>All auditing fields from AuditedEntity</li>
 *   <li>Proper equals, hashCode, and toString implementations</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "orders")
 * public class Order extends UuidEntity {
 *     private LocalDate orderDate;
 *     private String status;
 *
 *     // Getters and setters
 * }
 * </pre>
 */
@MappedSuperclass
public abstract class UuidEntity extends AuditedEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Gets the UUID primary key.
     *
     * @return the UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the UUID primary key.
     *
     * @param id the UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UuidEntity)) return false;

        UuidEntity that = (UuidEntity) o;

        // If the IDs are null, the entities are not equal
        if (id == null) return false;

        // Otherwise, compare by ID
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}