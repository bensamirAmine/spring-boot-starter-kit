package com.bensamir.starter.logging.filter;

import com.bensamir.starter.logging.util.ContentCachingRequestWrapper;
import com.bensamir.starter.logging.util.ContentCachingResponseWrapper;
import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter that logs incoming requests and outgoing responses.
 */
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final StarterKitProperties properties;

    public RequestResponseLoggingFilter(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // If request logging is not enabled, just pass through
        if (!properties.getLogging().getRequest().isEnabled() ||
                isPathExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap request and response for content caching
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        // Record request start time
        long startTime = System.currentTimeMillis();

        try {
            // Log request
            logRequest(wrappedRequest);

            // Execute the request
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // Calculate request duration
            long duration = System.currentTimeMillis() - startTime;

            // Log response
            logResponse(wrappedRequest, wrappedResponse, duration);

            // Copy content to the original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * Checks if the request path is excluded from logging.
     */
    private boolean isPathExcluded(String path) {
        List<String> excludePaths = properties.getLogging().getRequest().getExcludePaths();

        for (String pattern : excludePaths) {
            if (PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Logs the incoming request.
     */
    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder message = new StringBuilder()
                .append("REQUEST ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        // Add query string if enabled
        if (properties.getLogging().getRequest().isIncludeQueryString() &&
                request.getQueryString() != null) {
            message.append("?").append(request.getQueryString());
        }

        // Add headers if enabled
        if (properties.getLogging().getRequest().isIncludeHeaders()) {
            message.append(" Headers: {");
            Collections.list(request.getHeaderNames()).forEach(headerName ->
                    message.append(headerName).append("=")
                            .append(request.getHeader(headerName)).append(", "));
            if (message.charAt(message.length() - 2) == ',') {
                message.delete(message.length() - 2, message.length());
            }
            message.append("}");
        }

        // Add payload if enabled
        if (properties.getLogging().getRequest().isIncludePayload() &&
                isReadableContentType(request.getContentType())) {
            try {
                String payloadString = getMessagePayload(request.getContentAsByteArray(),
                        request.getCharacterEncoding(),
                        properties.getLogging().getRequest().getMaxPayloadLength());
                if (payloadString != null && !payloadString.isEmpty()) {
                    message.append(" Payload: ").append(payloadString);
                }
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse request payload", e);
            }
        }

        logger.info(message.toString());
    }

    /**
     * Logs the outgoing response.
     */
    private void logResponse(ContentCachingRequestWrapper request,
                             ContentCachingResponseWrapper response,
                             long duration) {
        StringBuilder message = new StringBuilder()
                .append("RESPONSE ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI())
                .append(" ")
                .append(response.getStatus())
                .append(" (").append(duration).append("ms)");

        // Add headers if enabled
        if (properties.getLogging().getRequest().isIncludeHeaders()) {
            message.append(" Headers: {");
            response.getHeaderNames().forEach(headerName ->
                    message.append(headerName).append("=")
                            .append(response.getHeader(headerName)).append(", "));
            if (message.charAt(message.length() - 2) == ',') {
                message.delete(message.length() - 2, message.length());
            }
            message.append("}");
        }

        // Add payload if enabled
        if (properties.getLogging().getRequest().isIncludePayload() &&
                isReadableContentType(response.getContentType())) {
            try {
                String payloadString = getMessagePayload(response.getContentAsByteArray(),
                        response.getCharacterEncoding(),
                        properties.getLogging().getRequest().getMaxPayloadLength());
                if (payloadString != null && !payloadString.isEmpty()) {
                    message.append(" Payload: ").append(payloadString);
                }
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse response payload", e);
            }
        }

        logger.info(message.toString());
    }

    /**
     * Checks if the content type is readable.
     */
    private boolean isReadableContentType(String contentType) {
        if (contentType == null) {
            return false;
        }

        return contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
                contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
                contentType.contains(MediaType.TEXT_PLAIN_VALUE) ||
                contentType.contains(MediaType.TEXT_HTML_VALUE) ||
                contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    /**
     * Gets the message payload as a string.
     */
    private String getMessagePayload(byte[] content, String contentEncoding, int maxLength)
            throws UnsupportedEncodingException {
        if (content.length == 0) {
            return null;
        }

        // Determine the encoding to use
        String encoding = contentEncoding != null ? contentEncoding : "UTF-8";

        // Limit the content length if necessary
        int length = Math.min(content.length, maxLength);

        // Convert to string
        String payload = new String(content, 0, length, encoding);

        // Add ellipsis if truncated
        if (content.length > maxLength) {
            payload += "...";
        }

        return payload;
    }
}