package com.bensamir.starter.logging.filter;

import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Enterprise-grade filter that adds essential diagnostic information to MDC context.
 * <p>
 * MDC (Mapped Diagnostic Context) enables correlation of log entries across components
 * that process the same request. This filter adds:
 * <ul>
 *   <li>Request ID - unique identifier for each request</li>
 *   <li>Correlation ID - honors existing correlation ID from headers</li>
 *   <li>User ID - authenticated user identifier when available</li>
 *   <li>Session ID - HTTP session identifier</li>
 *   <li>Client IP - originating IP address (respecting X-Forwarded-For)</li>
 * </ul>
 * <p>
 * This filter implements best practices:
 * <ul>
 *   <li>Honoring trace headers from upstream services</li>
 *   <li>Adding correlation IDs to response headers</li>
 *   <li>Supporting multiple common correlation header formats</li>
 *   <li>Proper cleanup of MDC context after request processing</li>
 * </ul>
 */
public class MdcFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(MdcFilter.class);

    private final StarterKitProperties properties;

    /**
     * Creates a new MDC filter.
     *
     * @param properties the starter kit properties
     */
    public MdcFilter(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Establish MDC context
            setupMdcContext(request, response);

            // Log the MDC context setup
            if (logger.isDebugEnabled()) {
                logger.debug("MDC context established for request to {}", request.getRequestURI());
            }

            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Always clean up MDC context to prevent leaks
            clearMdcContext();
        }
    }

    /**
     * Sets up the MDC context with request information.
     */
    private void setupMdcContext(HttpServletRequest request, HttpServletResponse response) {
        // 1. Request/Correlation ID
        String requestIdKey = properties.getLogging().getMdc().getRequestIdKey();
        String requestId = extractOrGenerateCorrelationId(request);
        MDC.put(requestIdKey, requestId);

        // Add correlation ID to response for cross-service tracing
        response.addHeader("X-Request-ID", requestId);
        response.addHeader("X-Correlation-ID", requestId);

        // 2. User information if authenticated
        addUserContext();

        // 3. Session ID if available
        addSessionId(request);

        // 4. Client IP address
        addClientIpAddress(request);

        // 5. Request URL info
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
    }

    /**
     * Extracts correlation ID from headers or generates a new one.
     * <p>
     * Recognizes many common correlation ID header formats.
     */
    private String extractOrGenerateCorrelationId(HttpServletRequest request) {
        // Common correlation ID headers
        String[] CORRELATION_HEADERS = new String[] {
                "X-Request-ID",
                "X-Correlation-ID",
                "X-Request-Id",
                "X-Correlation-Id",
                "X-Trace-ID",
                "Request-ID",
                "Correlation-ID",
                "TraceID",
                "X-B3-TraceId" // Used by Zipkin/Brave
        };

        // Try all common headers
        for (String header : CORRELATION_HEADERS) {
            String value = request.getHeader(header);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }

        // Generate new ID if none found
        return UUID.randomUUID().toString();
    }

    /**
     * Adds authenticated user information to MDC.
     */
    private void addUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            // Add user ID
            String userIdKey = properties.getLogging().getMdc().getUserIdKey();
            MDC.put(userIdKey, authentication.getName());

            // Add roles/authorities if configured
            if (properties.getLogging().getMdc().isIncludeUserRoles()) {
                String roles = authentication.getAuthorities().stream()
                        .map(Object::toString)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
                MDC.put("userRoles", roles);
            }
        }
    }

    /**
     * Adds session ID to MDC if available.
     */
    private void addSessionId(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            MDC.put("sessionId", request.getSession().getId());
        }
    }

    /**
     * Adds client IP address to MDC, respecting proxies.
     */
    private void addClientIpAddress(HttpServletRequest request) {
        if (properties.getLogging().getMdc().isIncludeClientIp()) {
            String clientIp = getClientIpAddress(request);
            MDC.put("clientIp", clientIp);
        }
    }

    /**
     * Extracts the client IP address, respecting X-Forwarded-For.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String headerName = "X-Forwarded-For";
        String ip = request.getHeader(headerName);

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // If multiple IPs, take the first one (client IP)
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * Clears all MDC context entries to prevent leaks.
     */
    private void clearMdcContext() {
        MDC.clear();
    }
}