package com.bensamir.starter.response;

public class ErrorResponse {
    private final String code;
    private final String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Static factory methods
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}