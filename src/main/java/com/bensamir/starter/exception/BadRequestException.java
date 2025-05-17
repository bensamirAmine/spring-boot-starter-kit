package com.bensamir.starter.exception;

/**
 * Exception thrown when a request contains invalid parameters or data.
 * <p>
 * This typically maps to a 400 Bad Request HTTP response.
 */
public class BadRequestException extends BaseException {
    private static final String DEFAULT_ERROR_CODE = "BAD_REQUEST";

    /**
     * Creates a new BadRequestException with the specified message.
     *
     * @param message the exception message
     */
    public BadRequestException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }

    /**
     * Creates a new BadRequestException with the specified message and custom error code.
     *
     * @param message the exception message
     * @param errorCode the custom error code
     */
    public BadRequestException(String message, String errorCode) {
        super(message, errorCode);
    }
}