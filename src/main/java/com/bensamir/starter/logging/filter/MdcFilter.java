package com.bensamir.starter.logging.filter;

import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that adds context information to MDC for logging.
 */
public class MdcFilter extends OncePerRequestFilter {

    private final StarterKitProperties properties;

    public MdcFilter(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // If MDC is not enabled, just pass through
            if (!properties.getLogging().getMdc().isEnabled()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Add request ID
            String requestIdKey = properties.getLogging().getMdc().getRequestIdKey();
            String requestId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(requestIdKey, requestId);

            // Add user ID if available
            String userIdKey = properties.getLogging().getMdc().getUserIdKey();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getPrincipal())) {
                MDC.put(userIdKey, authentication.getName());
            }

            // Add IP address if enabled
            if (properties.getLogging().getMdc().isIncludeIpAddress()) {
                String ipAddressKey = properties.getLogging().getMdc().getIpAddressKey();
                String ipAddress = getClientIpAddress(request);
                MDC.put(ipAddressKey, ipAddress);
            }

            // Set response header with request ID for tracing
            response.setHeader("X-Request-ID", requestId);

            filterChain.doFilter(request, response);
        } finally {
            // Always clear MDC after request completes
            MDC.clear();
        }
    }

    /**
     * Gets client IP address from request.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // If the IP has multiple comma-separated addresses, take the first one
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
}