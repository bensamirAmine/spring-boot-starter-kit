package com.bensamir.starter.persistence;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

/**
 * Base entity with auto-incremented Long ID.
 * <p>
 * This class provides:
 * <ul>
 *   <li>Auto-incremented Long ID field</li>
 *   <li>All auditing fields from AuditedEntity</li>
 *   <li>Proper equals, hashCode, and toString implementations</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "products")
 * public class Product extends BaseEntity {
 *     private String name;
 *     private BigDecimal price;
 *
 *     // Getters and setters
 * }
 * </pre>
 */
@MappedSuperclass
public abstract class BaseEntity extends AuditedEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Gets the primary key.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key.
     *
     * @param id the ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

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