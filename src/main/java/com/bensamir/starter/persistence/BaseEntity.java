package com.bensamir.starter.persistence;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base class for all JPA entities that use a Long as primary key.
 * Provides common fields that most entities need:
 * <ul>
 *     <li>id - auto-incremented primary key</li>
 *     <li>createdAt - creation timestamp (automatically set)</li>
 *     <li>updatedAt - last update timestamp (automatically updated)</li>
 *     <li>version - optimistic locking version</li>
 * </ul>
 *
 * Usage:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "users")
 * public class User extends BaseEntity {
 *     private String name;
 *     // Additional fields
 * }
 * </pre>
 *
 * @author Ben Samir
 * @see BaseUuidEntity
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}