package com.bensamir.starter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Objects;

/**
 * Standard response wrapper for all API endpoints.
 * <p>
 * This class provides a consistent structure for responses, with support for:
 * <ul>
 *   <li>Success/failure status</li>
 *   <li>Payload data</li>
 *   <li>Error information</li>
 *   <li>Metadata for additional context</li>
 *   <li>Consistent timestamps</li>
 * </ul>
 *
 * @param <T> The type of data in the response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;
    private final Instant timestamp;
    private final MetaData meta;

    private ApiResponse(boolean success, T data, ErrorResponse error, MetaData meta) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now();
        this.meta = meta;
    }

    /**
     * Creates a success response with data.
     *
     * @param data The payload data
     * @param <T> The type of the payload data
     * @return A success response containing the data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    /**
     * Creates a success response with data and metadata.
     *
     * @param data The payload data
     * @param meta Additional metadata
     * @param <T> The type of the payload data
     * @return A success response containing the data and metadata
     */
    public static <T> ApiResponse<T> success(T data, MetaData meta) {
        return new ApiResponse<>(true, data, null, meta);
    }

    /**
     * Creates an error response.
     *
     * @param error The error details
     * @param <T> The type of the payload data (null in error responses)
     * @return An error response containing the error details
     */
    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error, null);
    }

    /**
     * Creates an error response with metadata.
     *
     * @param error The error details
     * @param meta Additional metadata
     * @param <T> The type of the payload data (null in error responses)
     * @return An error response containing the error details and metadata
     */
    public static <T> ApiResponse<T> error(ErrorResponse error, MetaData meta) {
        return new ApiResponse<>(false, null, error, meta);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public MetaData getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return success == that.success &&
                Objects.equals(data, that.data) &&
                Objects.equals(error, that.error) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, data, error, timestamp, meta);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", data=" + data +
                ", error=" + error +
                ", timestamp=" + timestamp +
                ", meta=" + meta +
                '}';
    }
}