package com.bensamir.starter.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * A builder for creating standardized ResponseEntity objects with ApiResponse wrappers.
 * This utility class provides a fluent API for creating different kinds of responses:
 * <ul>
 *     <li>Success responses (200 OK)</li>
 *     <li>Created responses (201 Created)</li>
 *     <li>No Content responses (204 No Content)</li>
 *     <li>Error responses (4xx or 5xx)</li>
 *     <li>Paginated responses with metadata</li>
 * </ul>
 *
 * Usage:
 * <pre>
 * // Success response
 * return ResponseEntityBuilder.success(data);
 *
 * // Created response
 * return ResponseEntityBuilder.created(newResource);
 *
 * // Error response
 * return ResponseEntityBuilder.badRequest("VALIDATION_ERROR", "Invalid input");
 *
 * // Paginated response
 * return ResponseEntityBuilder.page(pageOfResults);
 * </pre>
 *
 * @author Ben Samir
 */
public class ResponseEntityBuilder {

    private ResponseEntityBuilder() {
        // Utility class, no instantiation
    }

    // Success responses
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.success(data));
    }

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

    public static ResponseEntity<ApiResponse<Void>> created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }

    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(null));
    }

    // Error responses
    public static <T> ResponseEntity<ApiResponse<T>> error(String code, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(ErrorResponse.of(code, message)));
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String code, String message) {
        return error(code, message, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String code, String message) {
        return error(code, message, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String code, String message) {
        return error(code, message, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String code, String message) {
        return error(code, message, HttpStatus.FORBIDDEN);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalError(String code, String message) {
        return error(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}