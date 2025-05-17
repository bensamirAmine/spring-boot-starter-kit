package com.bensamir.starter.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Container for additional response metadata.
 * <p>
 * This class provides a flexible structure for including additional
 * contextual information in API responses, such as:
 * <ul>
 *   <li>Pagination details</li>
 *   <li>Processing timestamps</li>
 *   <li>Counts and metrics</li>
 *   <li>Any other context-specific information</li>
 * </ul>
 */
public class MetaData {
    private final Map<String, Object> data;

    private MetaData(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }

    /**
     * Creates metadata from a map of values.
     *
     * @param data The metadata values
     * @return A metadata object
     */
    public static MetaData of(Map<String, Object> data) {
        return new MetaData(new HashMap<>(data));
    }

    /**
     * Creates an empty metadata object.
     *
     * @return An empty metadata object
     */
    public static MetaData empty() {
        return new MetaData(new HashMap<>());
    }

    /**
     * Creates metadata for pagination.
     *
     * @param totalElements Total number of elements
     * @param totalPages Total number of pages
     * @param page Current page number (0-based)
     * @param size Page size
     * @return Metadata with pagination information
     */
    public static MetaData pagination(long totalElements, int totalPages, int page, int size) {
        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("totalElements", totalElements);
        paginationData.put("totalPages", totalPages);
        paginationData.put("page", page);
        paginationData.put("size", size);
        return new MetaData(paginationData);
    }

    /**
     * Creates a builder for constructing metadata incrementally.
     *
     * @return A metadata builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets all metadata as a map.
     *
     * @return An unmodifiable view of all metadata
     */
    @JsonAnyGetter
    public Map<String, Object> getData() {
        return Collections.unmodifiableMap(data);
    }

    /**
     * Gets a specific metadata value.
     *
     * @param key The metadata key
     * @return The value, or null if not present
     */
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(data, metaData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "data=" + data +
                '}';
    }

    /**
     * Builder for creating MetaData objects incrementally.
     */
    public static class Builder {
        private final Map<String, Object> data = new HashMap<>();

        /**
         * Adds a metadata entry.
         *
         * @param key The metadata key
         * @param value The metadata value
         * @return This builder for chaining
         */
        @JsonAnySetter
        public Builder add(String key, Object value) {
            data.put(key, value);
            return this;
        }

        /**
         * Builds the metadata object.
         *
         * @return The constructed metadata
         */
        public MetaData build() {
            return new MetaData(data);
        }
    }
}