package com.bensamir.starter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

/**
 * Standardized error response structure.
 * <p>
 * This class provides a consistent format for error information, including:
 * <ul>
 *   <li>Error code - a machine-readable identifier</li>
 *   <li>Error message - a human-readable description</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String code;
    private final String message;
    private final String detail;

    private ErrorResponse(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    /**
     * Creates an error response with code and message.
     *
     * @param code The error code
     * @param message The error message
     * @return An error response
     */
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, null);
    }

    /**
     * Creates an error response with code, message, and additional details.
     *
     * @param code The error code
     * @param message The error message
     * @param detail Additional error details
     * @return An error response
     */
    public static ErrorResponse of(String code, String message, String detail) {
        return new ErrorResponse(code, message, detail);
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(detail, that.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, detail);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}