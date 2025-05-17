package com.bensamir.starter.exception;

/**
 * Exception thrown when a user is not authorized to access a resource.
 * <p>
 * This typically maps to a 401 Unauthorized HTTP response.
 */
public class UnauthorizedException extends BaseException {
    private static final String DEFAULT_ERROR_CODE = "UNAUTHORIZED";

    /**
     * Creates a new UnauthorizedException with the specified message.
     *
     * @param message the exception message
     */
    public UnauthorizedException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}