package com.bensamir.starter.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method execution.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {

    /**
     * Whether to log method parameters.
     */
    boolean logParameters() default true;

    /**
     * Whether to log return value.
     */
    boolean logReturnValue() default true;

    /**
     * Maximum length of parameter/return value logging.
     */
    int maxLength() default 1000;
}