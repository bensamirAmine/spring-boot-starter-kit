package com.bensamir.starter.exception;

import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Global exception handler that provides standardized error responses
 * for common exceptions.
 *
 * This handler:
 * <ul>
 *     <li>Formats exceptions into consistent API responses</li>
 *     <li>Maps exceptions to appropriate HTTP status codes</li>
 *     <li>Includes validation errors for MethodArgumentNotValidException</li>
 *     <li>Supports internationalization of error messages</li>
 *     <li>Conditionally includes stack traces based on configuration</li>
 * </ul>
 *
 * @author Ben Samir
 * @see ApiError
 * @see ResourceNotFoundException
 * @see BadRequestException
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final StarterKitProperties properties;
    private final ErrorMessageResolver messageResolver;

    public GlobalExceptionHandler(StarterKitProperties properties, ErrorMessageResolver messageResolver) {
        this.properties = properties;
        this.messageResolver = messageResolver;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        // Log the exception if enabled
        if (properties.getExceptionHandling().isLogExceptions()) {
            log.warn("Resource not found: {}", ex.getMessage());
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setErrorCode("RESOURCE_NOT_FOUND");
        apiError.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        apiError.setMessage(messageResolver.resolveMessage("resource.notfound", ex.getMessage()));
        apiError.setPath(request.getRequestURI());

        // Add stack trace if enabled
        addStackTraceIfEnabled(apiError, ex);

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {

        if (properties.getExceptionHandling().isLogExceptions()) {
            log.warn("Bad request: {}", ex.getMessage());
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorCode("BAD_REQUEST");
        apiError.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        apiError.setMessage(messageResolver.resolveMessage("bad.request", ex.getMessage()));
        apiError.setPath(request.getRequestURI());

        addStackTraceIfEnabled(apiError, ex);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        if (properties.getExceptionHandling().isLogExceptions()) {
            log.warn("Validation error in request to {}", request.getRequestURI());
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setErrorCode("VALIDATION_ERROR");
        apiError.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        apiError.setMessage(messageResolver.resolveMessage("validation.error", "Validation error"));
        apiError.setPath(request.getRequestURI());

        // Add validation errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            apiError.addValidationError(fieldName, errorMessage);
        });

        addStackTraceIfEnabled(apiError, ex);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        if (properties.getExceptionHandling().isLogExceptions()) {
            log.error("Unhandled exception occurred", ex);
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setErrorCode("INTERNAL_ERROR");
        apiError.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        apiError.setMessage(messageResolver.resolveMessage("internal.error", "An unexpected error occurred"));
        apiError.setPath(request.getRequestURI());

        addStackTraceIfEnabled(apiError, ex);

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addStackTraceIfEnabled(ApiError apiError, Exception ex) {
        if (properties.getExceptionHandling().isIncludeStackTrace()) {
            apiError.setStackTrace(
                    Arrays.stream(ex.getStackTrace())
                            .map(StackTraceElement::toString)
                            .collect(Collectors.toList())
            );
        }
    }
}