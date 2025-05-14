package com.bensamir.starter.security.annotation;

import com.bensamir.starter.security.core.model.SecurityRole;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to require ADMIN role for a method or class.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + SecurityRole.ADMIN + "')")
public @interface RequiresAdmin {
}