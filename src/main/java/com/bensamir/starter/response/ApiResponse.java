package com.bensamir.starter.response;

import java.time.Instant;

public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;
    private final Instant timestamp;
    private final MetaData meta;

    private ApiResponse(boolean success, T data, ErrorResponse error, MetaData meta) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now();
        this.meta = meta;
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, MetaData meta) {
        return new ApiResponse<>(true, data, null, meta);
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error, null);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public MetaData getMeta() {
        return meta;
    }
}