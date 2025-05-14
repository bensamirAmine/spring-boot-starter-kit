package com.bensamir.starter.logging.aspect;

import com.bensamir.starter.properties.StarterKitProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for method-level performance logging.
 */
@Aspect
public class PerformanceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceLoggingAspect.class);

    private final StarterKitProperties properties;

    public PerformanceLoggingAspect(StarterKitProperties properties) {
        this.properties = properties;
    }

    /**
     * Logs performance metrics for methods in monitored packages.
     */
    @Around("execution(* *(..)) && !execution(* com.bensamir.starter..*(..))")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // If performance logging is not enabled, just proceed
        if (!properties.getLogging().getPerformance().isEnabled() ||
                !isInMonitoredPackage(joinPoint)) {
            return joinPoint.proceed();
        }

        // Get method signature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        // Record start time
        long startTime = System.currentTimeMillis();

        try {
            // Execute the method
            return joinPoint.proceed();
        } finally {
            // Calculate duration
            long duration = System.currentTimeMillis() - startTime;

            // Log duration if it exceeds threshold or if in debug mode
            int threshold = properties.getLogging().getPerformance().getSlowExecutionThresholdMs();

            if (duration >= threshold) {
                logger.warn("SLOW EXECUTION: {}.{} took {}ms (threshold: {}ms)",
                        className, methodName, duration, threshold);
            } else {
                logger.debug("PERFORMANCE: {}.{} took {}ms",
                        className, methodName, duration);
            }
        }
    }

    /**
     * Checks if the method is in a monitored package.
     */
    private boolean isInMonitoredPackage(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();

        for (String packageName : properties.getLogging().getPerformance().getIncludePackages()) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }

        return false;
    }
}