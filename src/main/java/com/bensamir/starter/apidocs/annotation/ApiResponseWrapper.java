package com.bensamir.starter.apidocs.annotation;

import com.bensamir.starter.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to document standard API responses in OpenAPI documentation.
 * <p>
 * This annotation provides a consistent way to document common HTTP responses
 * in OpenAPI documentation, including:
 * <ul>
 *   <li>200 OK - Successful operation</li>
 *   <li>400 Bad Request - Invalid request</li>
 *   <li>401 Unauthorized - Authentication required</li>
 *   <li>403 Forbidden - Insufficient permissions</li>
 *   <li>404 Not Found - Resource not found</li>
 *   <li>500 Internal Server Error - Server error</li>
 * </ul>
 * <p>
 * Usage example:
 * <pre>
 * &#64;GetMapping("/users/{id}")
 * &#64;ApiResponseWrapper
 * public ResponseEntity&lt;ApiResponse&lt;UserDTO&gt;&gt; getUser(@PathVariable Long id) {
 *     // Implementation
 * }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Resource not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(schema = @Schema(implementation = ApiError.class)))
})
public @interface ApiResponseWrapper {
    // This is a marker annotation with responses defined in annotations above
}