package com.bensamir.starter.apidocs.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to document pageable parameters in OpenAPI.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY, name = "page", description = "Page number (0-based)",
        schema = @Schema(type = "integer", defaultValue = "0"))
@Parameter(in = ParameterIn.QUERY, name = "size", description = "Page size",
        schema = @Schema(type = "integer", defaultValue = "20"))
@Parameter(in = ParameterIn.QUERY, name = "sort", description = "Sorting criteria (e.g., id,desc)",
        schema = @Schema(type = "string"))
public @interface ApiPageable {
}