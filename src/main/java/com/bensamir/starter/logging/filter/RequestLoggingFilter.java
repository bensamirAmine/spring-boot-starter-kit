package com.bensamir.starter.logging.filter;

import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Enterprise-grade filter for comprehensive HTTP request and response logging.
 * <p>
 * This filter provides detailed logging of HTTP requests and responses with:
 * <ul>
 *   <li>Configurable verbosity levels</li>
 *   <li>Request/response headers logging</li>
 *   <li>Request/response body logging with size limits</li>
 *   <li>Accurate timing information</li>
 *   <li>Path-based exclusions</li>
 *   <li>Content type filtering</li>
 * </ul>
 * <p>
 * This implementation carefully balances information with performance:
 * <ul>
 *   <li>Using content caching to avoid input stream consumption</li>
 *   <li>Respecting content type for logging decisions</li>
 *   <li>Truncating large bodies to prevent memory issues</li>
 *   <li>Secure handling of sensitive headers</li>
 * </ul>
 * <p>
 * Configure via properties:
 * <pre>
 * starter-kit:
 *   logging:
 *     request:
 *       enabled: true
 *       include-headers: true
 *       include-payload: true
 *       max-payload-length: 10000
 *       exclude-paths: ["/actuator/**", "/swagger-ui/**"]
 * </pre>
 */
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // Headers that might contain sensitive information
    private static final String[] SENSITIVE_HEADERS = {
            "Authorization", "Cookie", "Set-Cookie", "X-API-Key"
    };

    private final StarterKitProperties properties;

    /**
     * Creates a new request logging filter.
     *
     * @param properties the starter kit properties
     */
    public RequestLoggingFilter(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip if disabled or path excluded
        if (!properties.getLogging().getRequest().isEnabled() ||
                isPathExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap request and response for content caching if payload logging is enabled
        boolean includePayload = properties.getLogging().getRequest().isIncludePayload();
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (includePayload) {
            requestToUse = new ContentCachingRequestWrapper(request);
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        // Start timer
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Log request
        logRequest(requestToUse);

        try {
            // Execute request
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            // Stop timer
            stopWatch.stop();

            // Log response
            logResponse(requestToUse, responseToUse, stopWatch.getTotalTimeMillis());

            // Copy content to original response if wrapped
            if (includePayload && responseToUse instanceof ContentCachingResponseWrapper) {
                ((ContentCachingResponseWrapper) responseToUse).copyBodyToResponse();
            }
        }
    }

    /**
     * Checks if a path should be excluded from logging.
     *
     * @param path the request path
     * @return true if the path should be excluded
     */
    private boolean isPathExcluded(String path) {
        return Arrays.stream(properties.getLogging().getRequest().getExcludePaths())
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    /**
     * Logs the incoming request with configured detail level.
     *
     * @param request the HTTP request
     */
    private void logRequest(HttpServletRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("Incoming request: ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        // Add query string if present
        String queryString = request.getQueryString();
        if (queryString != null) {
            message.append("?").append(queryString);
        }

        // Log basic request information
        logger.info(message.toString());

        // Log headers if enabled
        if (properties.getLogging().getRequest().isIncludeHeaders()) {
            logHeaders(request, "Request");
        }

        // Log payload if enabled and available
        if (properties.getLogging().getRequest().isIncludePayload() &&
                request instanceof ContentCachingRequestWrapper) {
            logPayload((ContentCachingRequestWrapper) request, "Request");
        }
    }

    /**
     * Logs the outgoing response with configured detail level.
     *
     * @param request the HTTP request (for context)
     * @param response the HTTP response
     * @param timeMs the request processing time in milliseconds
     */
    private void logResponse(HttpServletRequest request,
                             HttpServletResponse response,
                             long timeMs) {
        // Basic response information
        logger.info("Outgoing response: {} {} - {} ({} ms)",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                timeMs);

        // Log headers if enabled
        if (properties.getLogging().getRequest().isIncludeHeaders()) {
            logResponseHeaders(response);
        }

        // Log payload if enabled and available
        if (properties.getLogging().getRequest().isIncludePayload() &&
                response instanceof ContentCachingResponseWrapper) {
            logPayload((ContentCachingResponseWrapper) response, "Response");
        }
    }

    /**
     * Logs the headers from a request.
     *
     * @param request the HTTP request
     * @param prefix log message prefix
     */
    private void logHeaders(HttpServletRequest request, String prefix) {
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            String headerValue = getHeaderValue(request, headerName);
            logger.debug("{} Header: {} = {}", prefix, headerName, headerValue);
        });
    }

    /**
     * Logs the headers from a response.
     *
     * @param response the HTTP response
     */
    private void logResponseHeaders(HttpServletResponse response) {
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = getHeaderValue(response, headerName);
            logger.debug("Response Header: {} = {}", headerName, headerValue);
        });
    }

    /**
     * Gets a header value, masking sensitive headers.
     *
     * @param request the HTTP request
     * @param headerName the header name
     * @return the header value (masked if sensitive)
     */
    private String getHeaderValue(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        return maskSensitiveHeader(headerName, value);
    }

    /**
     * Gets a header value, masking sensitive headers.
     *
     * @param response the HTTP response
     * @param headerName the header name
     * @return the header value (masked if sensitive)
     */
    private String getHeaderValue(HttpServletResponse response, String headerName) {
        String value = response.getHeader(headerName);
        return maskSensitiveHeader(headerName, value);
    }

    /**
     * Masks sensitive header values.
     *
     * @param headerName the header name
     * @param value the header value
     * @return the masked value if sensitive, original otherwise
     */
    private String maskSensitiveHeader(String headerName, String value) {
        if (value == null) {
            return null;
        }

        for (String sensitiveHeader : SENSITIVE_HEADERS) {
            if (headerName.equalsIgnoreCase(sensitiveHeader)) {
                if (value.length() > 8) {
                    return value.substring(0, 4) + "..." + value.substring(value.length() - 4);
                } else {
                    return "********";
                }
            }
        }

        return value;
    }

    /**
     * Logs the payload of a request or response.
     *
     * @param wrapper the content caching wrapper
     * @param prefix log message prefix
     */
    private void logPayload(ContentCachingRequestWrapper wrapper, String prefix) {
        byte[] content = wrapper.getContentAsByteArray();
        if (content.length == 0) {
            return;
        }

        // Check if content type is suitable for logging
        String contentType = wrapper.getContentType();
        if (!isLoggedContentType(contentType)) {
            logger.debug("{} Body: (binary content type - not logged)", prefix);
            return;
        }

        // Get max payload length
        int maxPayloadLength = properties.getLogging().getRequest().getMaxPayloadLength();

        // Get content as string
        String contentAsString = getContentAsString(content, wrapper.getCharacterEncoding(), maxPayloadLength);

        // Log payload
        if (content.length > maxPayloadLength) {
            logger.debug("{} Body: {} (truncated to {} bytes)", prefix, contentAsString, maxPayloadLength);
        } else {
            logger.debug("{} Body: {}", prefix, contentAsString);
        }
    }

    /**
     * Logs the payload of a response.
     *
     * @param wrapper the content caching wrapper
     * @param prefix log message prefix
     */
    private void logPayload(ContentCachingResponseWrapper wrapper, String prefix) {
        byte[] content = wrapper.getContentAsByteArray();
        if (content.length == 0) {
            return;
        }

        // Check if content type is suitable for logging
        String contentType = wrapper.getContentType();
        if (!isLoggedContentType(contentType)) {
            logger.debug("{} Body: (binary content type - not logged)", prefix);
            return;
        }

        // Get max payload length
        int maxPayloadLength = properties.getLogging().getRequest().getMaxPayloadLength();

        // Get content as string
        String contentAsString = getContentAsString(content, wrapper.getCharacterEncoding(), maxPayloadLength);

        // Log payload
        if (content.length > maxPayloadLength) {
            logger.debug("{} Body: {} (truncated to {} bytes)", prefix, contentAsString, maxPayloadLength);
        } else {
            logger.debug("{} Body: {}", prefix, contentAsString);
        }
    }

    /**
     * Checks if a content type should be logged.
     *
     * @param contentType the content type
     * @return true if the content should be logged
     */
    private boolean isLoggedContentType(String contentType) {
        if (contentType == null) {
            return false;
        }

        return contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
                contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
                contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) ||
                contentType.contains(MediaType.TEXT_PLAIN_VALUE) ||
                contentType.contains(MediaType.TEXT_XML_VALUE) ||
                contentType.contains(MediaType.TEXT_HTML_VALUE);
    }

    /**
     * Gets content as a string, handling encoding and truncation.
     *
     * @param content the content bytes
     * @param encoding the character encoding
     * @param maxLength the maximum content length
     * @return the content as a string
     */
    private String getContentAsString(byte[] content, String encoding, int maxLength) {
        if (content.length == 0) {
            return "";
        }

        int length = Math.min(content.length, maxLength);

        try {
            String charsetName = encoding != null ? encoding : "UTF-8";
            return new String(content, 0, length, charsetName);
        } catch (UnsupportedEncodingException e) {
            return new String(content, 0, length);
        }
    }
}