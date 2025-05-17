package com.bensamir.starter.exception;

/**
 * Base abstract exception class for standardized error handling.
 * <p>
 * All custom exceptions should extend this class to ensure consistency
 * in error codes and messaging across the application.
 */
public abstract class BaseException extends RuntimeException {
    private final String errorCode;

    /**
     * Creates a new BaseException with the specified message and error code.
     *
     * @param message the exception message
     * @param errorCode the standardized error code
     */
    protected BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates a new BaseException with the specified message, cause, and error code.
     *
     * @param message the exception message
     * @param cause the cause of this exception
     * @param errorCode the standardized error code
     */
    protected BaseException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}