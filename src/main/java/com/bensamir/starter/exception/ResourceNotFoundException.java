package com.bensamir.starter.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * This typically maps to a 404 Not Found HTTP response.
 */
public class ResourceNotFoundException extends BaseException {
    private static final String DEFAULT_ERROR_CODE = "RESOURCE_NOT_FOUND";

    /**
     * Creates a new ResourceNotFoundException with the specified message.
     *
     * @param message the exception message
     */
    public ResourceNotFoundException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }

    /**
     * Creates a new ResourceNotFoundException with details about the missing resource.
     *
     * @param resourceName the name of the resource type
     * @param fieldName the name of the field used for lookup
     * @param fieldValue the value of the field used for lookup
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
                DEFAULT_ERROR_CODE);
    }
}