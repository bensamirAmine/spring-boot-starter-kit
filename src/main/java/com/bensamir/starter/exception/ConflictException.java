package com.bensamir.starter.exception;

/**
 * Exception thrown when a request would result in a conflict.
 * <p>
 * This typically maps to a 409 Conflict HTTP response.
 */
public class ConflictException extends BaseException {
    private static final String DEFAULT_ERROR_CODE = "CONFLICT";

    /**
     * Creates a new ConflictException with the specified message.
     *
     * @param message the exception message
     */
    public ConflictException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}