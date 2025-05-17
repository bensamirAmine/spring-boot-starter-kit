package com.bensamir.starter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base class for audited entities.
 * <p>
 * This class provides common auditing fields:
 * <ul>
 *   <li>createdAt - creation timestamp (automatically set)</li>
 *   <li>updatedAt - last update timestamp (automatically updated)</li>
 *   <li>createdBy - user who created the entity (if available)</li>
 *   <li>updatedBy - user who last updated the entity (if available)</li>
 *   <li>version - optimistic locking version counter</li>
 * </ul>
 * <p>
 * This base class does not include an ID field, allowing entities to define
 * their own ID strategy (auto-increment, UUID, composite keys, etc.).
 * <p>
 * Usage example with a Long ID:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "products")
 * public class Product extends AuditedEntity {
 *     &#64;Id
 *     &#64;GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Long id;
 *
 *     private String name;
 *     private BigDecimal price;
 *
 *     // Getters and setters
 * }
 * </pre>
 * <p>
 * Usage example with a UUID ID:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "orders")
 * public class Order extends AuditedEntity {
 *     &#64;Id
 *     &#64;GeneratedValue(strategy = GenerationType.UUID)
 *     private UUID id;
 *
 *     private LocalDate orderDate;
 *     private String status;
 *
 *     // Getters and setters
 * }
 * </pre>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Gets the timestamp when this entity was created.
     *
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the timestamp when this entity was last updated.
     *
     * @return the last update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Gets the user who created this entity.
     *
     * @return the creator username or ID
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets the user who last updated this entity.
     *
     * @return the last updater username or ID
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Gets the optimistic locking version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the optimistic locking version.
     *
     * @param version the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}