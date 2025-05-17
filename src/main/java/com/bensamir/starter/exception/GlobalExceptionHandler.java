package com.bensamir.starter.exception;


import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * Global exception handler that provides standardized error responses for exceptions.
 * <p>
 * This handler:
 * <ul>
 *   <li>Maps exceptions to appropriate HTTP status codes</li>
 *   <li>Formats exceptions into consistent API error responses</li>
 *   <li>Includes validation errors for invalid requests</li>
 *   <li>Optionally includes stack traces for debugging</li>
 *   <li>Logs exceptions at appropriate levels</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final StarterKitProperties properties;
    private final ErrorMessageResolver messageResolver;

    /**
     * Creates a new GlobalExceptionHandler.
     *
     * @param properties the starter kit properties
     * @param messageResolver the error message resolver
     */
    public GlobalExceptionHandler(StarterKitProperties properties, ErrorMessageResolver messageResolver) {
        this.properties = properties;
        this.messageResolver = messageResolver;
    }

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 404 response with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        logException(ex, "Resource not found: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.NOT_FOUND,
                ex.getErrorCode(),
                messageResolver.resolveMessage("resource.notfound", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {

        logException(ex, "Bad request: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                ex.getErrorCode(),
                messageResolver.resolveMessage("bad.request", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UnauthorizedException and Spring Security's AuthenticationException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 401 response with error details
     */
    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class})
    public ResponseEntity<ApiError> handleUnauthorizedException(
            Exception ex, HttpServletRequest request) {

        String errorCode = ex instanceof UnauthorizedException
                ? ((UnauthorizedException) ex).getErrorCode()
                : "UNAUTHORIZED";

        logException(ex, "Unauthorized access: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.UNAUTHORIZED,
                errorCode,
                messageResolver.resolveMessage("unauthorized", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles ForbiddenException and Spring Security's AccessDeniedException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 403 response with error details
     */
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<ApiError> handleForbiddenException(
            Exception ex, HttpServletRequest request) {

        String errorCode = ex instanceof ForbiddenException
                ? ((ForbiddenException) ex).getErrorCode()
                : "FORBIDDEN";

        logException(ex, "Forbidden access: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.FORBIDDEN,
                errorCode,
                messageResolver.resolveMessage("forbidden", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles ConflictException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 409 response with error details
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(
            ConflictException ex, HttpServletRequest request) {

        logException(ex, "Conflict: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.CONFLICT,
                ex.getErrorCode(),
                messageResolver.resolveMessage("conflict", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles validation exceptions from @Valid annotations.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        logException(ex, "Validation error in request to {}", request.getRequestURI(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                messageResolver.resolveMessage("validation.error", "Validation error"),
                request
        );

        // Add validation errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError
                    ? ((FieldError) error).getField()
                    : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            apiError.addValidationError(fieldName, errorMessage);
        });

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing request parameter exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        logException(ex, "Missing parameter: {}", ex.getParameterName(), LogLevel.WARN);

        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                "MISSING_PARAMETER",
                messageResolver.resolveMessage("missing.parameter", message, ex.getParameterName()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles parameter type mismatch exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        logException(ex, "Type mismatch: {} for parameter '{}'",
                ex.getMessage(), ex.getName(), LogLevel.WARN);

        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "unknown";

        String message = String.format("Parameter '%s' should be of type %s",
                ex.getName(), requiredType);

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                "TYPE_MISMATCH",
                messageResolver.resolveMessage("type.mismatch", message,
                        ex.getName(), requiredType),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Spring's BadRequestException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        logException(ex, "Bad request - message not readable: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                "MESSAGE_NOT_READABLE",
                messageResolver.resolveMessage("message.not.readable",
                        "Unable to read request body: " + ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Spring's BadRequestException.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 400 response with error details
     */
    @ExceptionHandler(org.apache.coyote.BadRequestException.class)
    public ResponseEntity<ApiError> handleSpringBadRequest(
            org.apache.coyote.BadRequestException ex, HttpServletRequest request) {

        logException(ex, "Bad request from Spring: {}", ex.getMessage(), LogLevel.WARN);

        ApiError apiError = createApiError(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                messageResolver.resolveMessage("bad.request", ex.getMessage()),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unhandled exceptions.
     *
     * @param ex the exception
     * @param request the HTTP request
     * @return a 500 response with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        logException(ex, "Unhandled exception occurred: {}", ex.getMessage(), LogLevel.ERROR);

        ApiError apiError = createApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                messageResolver.resolveMessage("internal.error", "An unexpected error occurred"),
                request
        );

        addStackTraceIfEnabled(apiError, ex);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper methods

    /**
     * Creates an ApiError with standard fields.
     *
     * @param status the HTTP status
     * @param errorCode the error code
     * @param message the error message
     * @param request the HTTP request
     * @return the created ApiError
     */
    private ApiError createApiError(HttpStatus status, String errorCode, String message,
                                    HttpServletRequest request) {
        return new ApiError(
                status.value(),
                errorCode,
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    /**
     * Adds stack trace information to the ApiError if enabled in configuration.
     *
     * @param apiError the API error
     * @param ex the exception
     */
    private void addStackTraceIfEnabled(ApiError apiError, Exception ex) {
        if (properties.getExceptionHandling().isIncludeStackTrace()) {
            apiError.addStackTraceFromException(ex);
        }
    }

    /**
     * Logs an exception with the appropriate log level if enabled.
     *
     * @param ex the exception
     * @param format the log message format
     * @param arg the log message argument
     * @param level the log level
     */
    private void logException(Exception ex, String format, Object arg, LogLevel level) {
        if (!properties.getExceptionHandling().isLogExceptions()) {
            return;
        }

        switch (level) {
            case ERROR -> log.error(format, arg, ex);
            case WARN -> log.warn(format, arg);
            case INFO -> log.info(format, arg);
            case DEBUG -> log.debug(format, arg);
        }
    }

    /**
     * Logs an exception with the appropriate log level if enabled.
     *
     * @param ex the exception
     * @param format the log message format
     * @param arg1 the first log message argument
     * @param arg2 the second log message argument
     * @param level the log level
     */
    private void logException(Exception ex, String format, Object arg1, Object arg2, LogLevel level) {
        if (!properties.getExceptionHandling().isLogExceptions()) {
            return;
        }

        switch (level) {
            case ERROR -> log.error(format, arg1, arg2, ex);
            case WARN -> log.warn(format, arg1, arg2);
            case INFO -> log.info(format, arg1, arg2);
            case DEBUG -> log.debug(format, arg1, arg2);
        }
    }

    /**
     * Log levels for exception logging.
     */
    private enum LogLevel {
        ERROR, WARN, INFO, DEBUG
    }
}