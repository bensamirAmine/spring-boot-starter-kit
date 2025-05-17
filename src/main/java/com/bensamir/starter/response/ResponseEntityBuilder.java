package com.bensamir.starter.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for building standardized ResponseEntity objects.
 * <p>
 * This builder ensures consistent API responses across all endpoints by providing
 * factory methods for common response types:
 * <ul>
 *   <li>Success responses (200 OK)</li>
 *   <li>Created responses (201 Created)</li>
 *   <li>No Content responses (204 No Content)</li>
 *   <li>Error responses (4xx, 5xx)</li>
 *   <li>Paginated responses</li>
 * </ul>
 */
public final class ResponseEntityBuilder {

    private ResponseEntityBuilder() {
        // Private constructor to prevent instantiation
    }

    // Success responses

    /**
     * Creates a success response with data (HTTP 200 OK).
     *
     * @param data The response data
     * @param <T> The type of data
     * @return A ResponseEntity with the wrapped data
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * Creates a success response with data and a custom HTTP status.
     *
     * @param data The response data
     * @param status The HTTP status
     * @param <T> The type of data
     * @return A ResponseEntity with the wrapped data and custom status
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.success(data));
    }

    /**
     * Creates a paginated response with metadata (HTTP 200 OK).
     *
     * @param page The Spring Data Page
     * @param <T> The type of content items
     * @return A ResponseEntity with the wrapped page and pagination metadata
     */
    public static <T> ResponseEntity<ApiResponse<PageResponse<T>>> page(Page<T> page) {
        PageResponse<T> pageResponse = PageResponse.of(page);
        MetaData metaData = MetaData.pagination(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
        return ResponseEntity.ok(ApiResponse.success(pageResponse, metaData));
    }

    /**
     * Creates a success response for resource creation (HTTP 201 Created).
     *
     * @return A ResponseEntity with no data
     */
    public static ResponseEntity<ApiResponse<Void>> created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    /**
     * Creates a success response for resource creation with the created resource (HTTP 201 Created).
     *
     * @param data The created resource
     * @param <T> The type of resource
     * @return A ResponseEntity with the created resource
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }

    /**
     * Creates a success response for operations that don't return data (HTTP 204 No Content).
     *
     * @return A ResponseEntity with no content
     */
    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(null));
    }

    // Error responses

    /**
     * Creates an error response with a custom HTTP status.
     *
     * @param code The error code
     * @param message The error message
     * @param status The HTTP status
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String code, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(ErrorResponse.of(code, message)));
    }

    /**
     * Creates an error response with a custom HTTP status and detailed error information.
     *
     * @param code The error code
     * @param message The error message
     * @param detail Additional error details
     * @param status The HTTP status
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with detailed error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String code, String message, String detail, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(ErrorResponse.of(code, message, detail)));
    }

    /**
     * Creates an error response with metadata and a custom HTTP status.
     *
     * @param code The error code
     * @param message The error message
     * @param metadata Additional metadata
     * @param status The HTTP status
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with error information and metadata
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String code, String message, Map<String, Object> metadata, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(ErrorResponse.of(code, message), MetaData.of(metadata)));
    }

    /**
     * Creates a bad request error response (HTTP 400 Bad Request).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with bad request error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String code, String message) {
        return error(code, message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a not found error response (HTTP 404 Not Found).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with not found error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String code, String message) {
        return error(code, message, HttpStatus.NOT_FOUND);
    }

    /**
     * Creates an unauthorized error response (HTTP 401 Unauthorized).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with unauthorized error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String code, String message) {
        return error(code, message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Creates a forbidden error response (HTTP 403 Forbidden).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with forbidden error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String code, String message) {
        return error(code, message, HttpStatus.FORBIDDEN);
    }

    /**
     * Creates an internal server error response (HTTP 500 Internal Server Error).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with internal server error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> internalError(String code, String message) {
        return error(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a conflict error response (HTTP 409 Conflict).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with conflict error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> conflict(String code, String message) {
        return error(code, message, HttpStatus.CONFLICT);
    }

    /**
     * Creates a service unavailable error response (HTTP 503 Service Unavailable).
     *
     * @param code The error code
     * @param message The error message
     * @param <T> The type parameter (null for error responses)
     * @return A ResponseEntity with service unavailable error information
     */
    public static <T> ResponseEntity<ApiResponse<T>> serviceUnavailable(String code, String message) {
        return error(code, message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}