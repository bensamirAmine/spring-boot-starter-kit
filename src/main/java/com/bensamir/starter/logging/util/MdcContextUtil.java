package com.bensamir.starter.logging.util;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Utility for propagating MDC context to other threads.
 * <p>
 * MDC context is thread-local, so it doesn't automatically propagate to
 * child threads, async executors, etc. This utility helps capture and
 * reestablish MDC context across thread boundaries.
 * <p>
 * Usage example:
 * <pre>
 * // In original thread with MDC context
 * Runnable task = MdcContextUtil.wrap(() -> {
 *     // MDC context is available in this runnable
 *     logger.info("Working in background thread");
 * });
 *
 * // Execute in another thread
 * executorService.submit(task);
 * </pre>
 */
public final class MdcContextUtil {

    private MdcContextUtil() {
        // Utility class, no instantiation
    }

    /**
     * Wraps a Runnable to propagate MDC context.
     *
     * @param runnable the original runnable
     * @return a wrapped runnable with MDC context
     */
    public static Runnable wrap(Runnable runnable) {
        // Capture current MDC context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            // Backup the target thread's MDC context (if any)
            Map<String, String> previousContext = MDC.getCopyOfContextMap();

            try {
                // Set up MDC context in target thread
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear();
                }

                // Execute the original runnable
                runnable.run();
            } finally {
                // Restore previous context
                if (previousContext != null) {
                    MDC.setContextMap(previousContext);
                } else {
                    MDC.clear();
                }
            }
        };
    }

    /**
     * Wraps a Callable to propagate MDC context.
     *
     * @param callable the original callable
     * @param <V> the callable return type
     * @return a wrapped callable with MDC context
     */
    public static <V> Callable<V> wrap(Callable<V> callable) {
        // Capture current MDC context
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            // Backup the target thread's MDC context (if any)
            Map<String, String> previousContext = MDC.getCopyOfContextMap();

            try {
                // Set up MDC context in target thread
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear();
                }

                // Execute the original callable
                return callable.call();
            } finally {
                // Restore previous context
                if (previousContext != null) {
                    MDC.setContextMap(previousContext);
                } else {
                    MDC.clear();
                }
            }
        };
    }
}