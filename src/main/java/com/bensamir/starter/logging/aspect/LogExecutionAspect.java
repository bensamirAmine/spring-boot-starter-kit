package com.bensamir.starter.logging.aspect;

import com.bensamir.starter.logging.annotation.LogExecution;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Aspect for handling LogExecution annotations.
 */
@Aspect
@Order(0)
public class LogExecutionAspect {

    @Around("@annotation(com.bensamir.starter.logging.annotation.LogExecution) || " +
            "@within(com.bensamir.starter.logging.annotation.LogExecution)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method and annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        // Try to get method-level annotation, then fall back to class-level
        LogExecution logExecution = method.getAnnotation(LogExecution.class);
        if (logExecution == null) {
            logExecution = targetClass.getAnnotation(LogExecution.class);
        }

        // Get logger for the class
        Logger logger = LoggerFactory.getLogger(targetClass);

        // If not enabled or not logging, just proceed
        if (logExecution == null || !logger.isInfoEnabled()) {
            return joinPoint.proceed();
        }

        // Log method entry with parameters if enabled
        if (logExecution.logParameters()) {
            Object[] args = joinPoint.getArgs();
            String params = formatParameters(args, logExecution.maxLength());
            logger.info("Entering {}.{}({})",
                    targetClass.getSimpleName(), method.getName(), params);
        } else {
            logger.info("Entering {}.{}", targetClass.getSimpleName(), method.getName());
        }

        try {
            // Execute method
            Object result = joinPoint.proceed();

            // Log method exit with return value if enabled
            if (logExecution.logReturnValue() && !method.getReturnType().equals(Void.TYPE)) {
                String resultStr = formatReturnValue(result, logExecution.maxLength());
                logger.info("Exiting {}.{} with result: {}",
                        targetClass.getSimpleName(), method.getName(), resultStr);
            } else {
                logger.info("Exiting {}.{}", targetClass.getSimpleName(), method.getName());
            }

            return result;
        } catch (Throwable ex) {
            // Log exception
            logger.error("Exception in {}.{}: {}",
                    targetClass.getSimpleName(), method.getName(), ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Formats method parameters for logging.
     */
    private String formatParameters(Object[] args, int maxLength) {
        if (args == null || args.length == 0) {
            return "";
        }

        return Arrays.stream(args)
                .map(arg -> formatValue(arg, maxLength))
                .collect(Collectors.joining(", "));
    }

    /**
     * Formats return value for logging.
     */
    private String formatReturnValue(Object result, int maxLength) {
        return formatValue(result, maxLength);
    }

    /**
     * Formats a value for logging, limiting length and handling nulls.
     */
    private String formatValue(Object value, int maxLength) {
        if (value == null) {
            return "null";
        }

        String str = value.toString();
        if (str.length() > maxLength) {
            return str.substring(0, maxLength) + "...";
        }

        return str;
    }
}