package com.bensamir.starter.exception;

/**
 * Exception thrown when a user doesn't have permission to perform an action.
 * <p>
 * This typically maps to a 403 Forbidden HTTP response.
 */
public class ForbiddenException extends BaseException {
    private static final String DEFAULT_ERROR_CODE = "FORBIDDEN";

    /**
     * Creates a new ForbiddenException with the specified message.
     *
     * @param message the exception message
     */
    public ForbiddenException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}