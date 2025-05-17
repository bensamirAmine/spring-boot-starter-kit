package com.bensamir.starter.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Standard error response format for API exceptions.
 * <p>
 * This class provides a consistent structure for error responses, including:
 * <ul>
 *   <li>HTTP status code and error name</li>
 *   <li>Error code for machine-readable identification</li>
 *   <li>Human-readable error message</li>
 *   <li>Request path that caused the error</li>
 *   <li>Timestamp when the error occurred</li>
 *   <li>Optional validation errors for form validation failures</li>
 *   <li>Optional stack trace for debugging (configurable)</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private LocalDateTime timestamp;
    private int status;
    private String errorCode;
    private String error;
    private String message;
    private String path;
    private List<String> stackTrace;
    private List<ValidationError> validationErrors;

    /**
     * Creates a new ApiError with the current timestamp.
     */
    public ApiError() {
        this.timestamp = LocalDateTime.now();
        this.validationErrors = new ArrayList<>();
        this.stackTrace = new ArrayList<>();
    }

    /**
     * Creates a new ApiError with all fields.
     *
     * @param status HTTP status code
     * @param errorCode Machine-readable error code
     * @param error HTTP status text
     * @param message Human-readable error message
     * @param path Request path that caused the error
     */
    public ApiError(int status, String errorCode, String error, String message, String path) {
        this();
        this.status = status;
        this.errorCode = errorCode;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Adds a validation error.
     *
     * @param field The field that failed validation
     * @param message The validation error message
     * @return This ApiError for method chaining
     */
    public ApiError addValidationError(String field, String message) {
        this.validationErrors.add(new ValidationError(field, message));
        return this;
    }

    /**
     * Adds a stack trace element.
     *
     * @param trace The stack trace line
     * @return This ApiError for method chaining
     */
    public ApiError addStackTrace(String trace) {
        this.stackTrace.add(trace);
        return this;
    }

    /**
     * Sets the stack trace from an exception.
     *
     * @param ex The exception
     * @return This ApiError for method chaining
     */
    public ApiError addStackTraceFromException(Exception ex) {
        for (StackTraceElement element : ex.getStackTrace()) {
            this.stackTrace.add(element.toString());
        }
        return this;
    }

    // Getters and setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(List<String> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return status == apiError.status &&
                Objects.equals(errorCode, apiError.errorCode) &&
                Objects.equals(path, apiError.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorCode, path);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", validationErrors=" + validationErrors.size() +
                '}';
    }

    /**
     * Nested class representing a validation error on a specific field.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationError {
        private final String field;
        private final String message;

        /**
         * Creates a new ValidationError.
         *
         * @param field The field that failed validation
         * @param message The validation error message
         */
        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValidationError that = (ValidationError) o;
            return Objects.equals(field, that.field) &&
                    Objects.equals(message, that.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field, message);
        }

        @Override
        public String toString() {
            return "ValidationError{" +
                    "field='" + field + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}