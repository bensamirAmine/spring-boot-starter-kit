# Exception Handling

## Overview

The exception handling component provides a standardized way to handle and format exceptions across your application. It ensures consistent error responses for all API endpoints and simplifies error handling.

## Features

- Global exception handler for centralizing error handling
- Standard exception types for common error scenarios
- Consistent error response format with detailed information
- Support for validation errors with field-level details
- Customizable error messages with internationalization (i18n) support
- Error logging with configurable levels
- Stack trace inclusion for development environments

## Configuration

Configure the exception handling component in your `application.yml`:

```yaml
starter-kit:
  exception-handling:
    enabled: true                 # Enable/disable the entire component
    log-exceptions: true          # Log exceptions to the console/log file
    include-stack-trace: false    # Include stack traces in responses (for development)
    enable-i18n: false            # Enable internationalization for error messages
    default-messages:
      resource.notfound: "Resource not found"
      bad.request: "Bad request"
      validation.error: "Validation error"
      internal.error: "An unexpected error occurred"
```

## Standard Exception Types

The component provides several standard exception types:

### ResourceNotFoundException

Thrown when a requested resource does not exist. Returns a 404 Not Found response.

```java
// Simple version
throw new ResourceNotFoundException("User not found");

// Detailed version
throw new ResourceNotFoundException("User", "id", userId);
// Results in: "User not found with id: 123"
```

### BadRequestException

Thrown when the request contains invalid parameters. Returns a 400 Bad Request response.

```java
throw new BadRequestException("Invalid input data");
```

### BaseException

A base class that you can extend to create your own exception types.

```java
public class PaymentFailedException extends BaseException {
    public PaymentFailedException(String message) {
        super(message, "PAYMENT_FAILED");
    }
}
```

## Error Response Format

All error responses follow this standard format:

```json
{
  "timestamp": "2023-05-20T10:30:45.123Z",
  "status": 404,
  "errorCode": "RESOURCE_NOT_FOUND",
  "error": "Not Found",
  "message": "User not found with id: 123",
  "path": "/api/users/123"
}
```

For validation errors, the response includes additional field-level details:

```json
{
  "timestamp": "2023-05-20T10:30:45.123Z",
  "status": 400,
  "errorCode": "VALIDATION_ERROR",
  "error": "Bad Request",
  "message": "Validation error",
  "path": "/api/users",
  "validationErrors": [
    {
      "field": "email",
      "message": "must be a well-formed email address"
    },
    {
      "field": "name",
      "message": "must not be blank"
    }
  ]
}
```

## Internationalization (i18n)

To enable internationalization for error messages:

1. Set `enable-i18n: true` in your configuration
2. Create message properties files:

```
# src/main/resources/messages/error-messages_en.properties
resource.notfound=Resource not found
bad.request=Bad request

# src/main/resources/messages/error-messages_fr.properties
resource.notfound=Ressource non trouvée
bad.request=Requête invalide
```

## Custom Exception Handlers

You can add custom exception handlers for your own exception types:

```java
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ApiError> handlePaymentFailedException(
            PaymentFailedException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_GATEWAY.value());
        apiError.setErrorCode("PAYMENT_FAILED");
        apiError.setError("Payment Gateway Error");
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_GATEWAY);
    }
}
```

## Integration with Other Components

The exception handling component integrates seamlessly with:

- **Response Utilities**: Uses the same response format
- **Base Controllers**: Automatically handles exceptions thrown by controllers
- **Security Framework**: Formats authentication and authorization errors
- **Logging Configuration**: Logs errors with appropriate levels

## Advanced Usage

### Including Stack Traces

For development environments, you can include stack traces in error responses:

```yaml
starter-kit:
  exception-handling:
    include-stack-trace: true
```

### Error Logging

The component logs exceptions with appropriate severity levels:
- Client errors (4xx) are logged as WARN
- Server errors (5xx) are logged as ERROR
- For sensitive operations, you can explicitly log at different levels:

```java
try {
    // Sensitive operation
} catch (Exception e) {
    logger.error("Critical error during payment processing", e);
    throw new PaymentFailedException("Payment processing failed");
}
```