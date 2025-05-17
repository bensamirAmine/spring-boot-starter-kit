package com.bensamir.starter.logging.properties;

import java.util.ArrayList;
import java.util.List;

public class LoggingProperties {
    private boolean enabled = true;
    private final RequestLoggingProperties request = new RequestLoggingProperties();
    private final PerformanceLoggingProperties performance = new PerformanceLoggingProperties();
    private final MdcProperties mdc = new MdcProperties();
    private final JsonLoggingProperties json = new JsonLoggingProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RequestLoggingProperties getRequest() {
        return request;
    }

    public PerformanceLoggingProperties getPerformance() {
        return performance;
    }

    public MdcProperties getMdc() {
        return mdc;
    }

    public JsonLoggingProperties getJson() {
        return json;
    }

    /**
     * Request logging configuration.
     */
    public static class RequestLoggingProperties {
        private boolean enabled = true;
        private boolean includeHeaders = true;
        private boolean includePayload = true;
        private int maxPayloadLength = 10000;
        private String[] excludePaths = {"/actuator/**", "/swagger-ui/**", "/v3/api-docs/**"};

        /**
         * Returns whether request logging is enabled.
         *
         * @return true if request logging is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether request logging is enabled.
         *
         * @param enabled true to enable request logging
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Returns whether to include headers in request logs.
         *
         * @return true if headers should be included
         */
        public boolean isIncludeHeaders() {
            return includeHeaders;
        }

        /**
         * Sets whether to include headers in request logs.
         *
         * @param includeHeaders true to include headers
         */
        public void setIncludeHeaders(boolean includeHeaders) {
            this.includeHeaders = includeHeaders;
        }

        /**
         * Returns whether to include request and response payloads in logs.
         *
         * @return true if payloads should be included
         */
        public boolean isIncludePayload() {
            return includePayload;
        }

        /**
         * Sets whether to include request and response payloads in logs.
         *
         * @param includePayload true to include payloads
         */
        public void setIncludePayload(boolean includePayload) {
            this.includePayload = includePayload;
        }

        /**
         * Returns the maximum payload length to log.
         *
         * @return maximum payload length in bytes
         */
        public int getMaxPayloadLength() {
            return maxPayloadLength;
        }

        /**
         * Sets the maximum payload length to log.
         *
         * @param maxPayloadLength maximum payload length in bytes
         */
        public void setMaxPayloadLength(int maxPayloadLength) {
            this.maxPayloadLength = maxPayloadLength;
        }

        /**
         * Returns the paths to exclude from request logging.
         *
         * @return array of path patterns to exclude
         */
        public String[] getExcludePaths() {
            return excludePaths;
        }

        /**
         * Sets the paths to exclude from request logging.
         *
         * @param excludePaths array of path patterns to exclude
         */
        public void setExcludePaths(String[] excludePaths) {
            this.excludePaths = excludePaths;
        }
    }

    public static class PerformanceLoggingProperties {
        private boolean enabled = true;
        private int slowExecutionThresholdMs = 1000;
        private List<String> includePackages = new ArrayList<>();

        public PerformanceLoggingProperties() {
            // Default package to monitor (your application)
            includePackages.add("com.example");
        }

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getSlowExecutionThresholdMs() {
            return slowExecutionThresholdMs;
        }

        public void setSlowExecutionThresholdMs(int slowExecutionThresholdMs) {
            this.slowExecutionThresholdMs = slowExecutionThresholdMs;
        }

        public List<String> getIncludePackages() {
            return includePackages;
        }

        public void setIncludePackages(List<String> includePackages) {
            this.includePackages = includePackages;
        }
    }

    /**
     * MDC context configuration.
     */
    public static class MdcProperties {
        private boolean enabled = true;
        private String requestIdKey = "requestId";
        private String userIdKey = "userId";
        private boolean includeClientIp = true;
        private boolean includeUserRoles = false;

        /**
         * Returns whether MDC context tracking is enabled.
         *
         * @return true if MDC context is enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets whether MDC context tracking is enabled.
         *
         * @param enabled true to enable MDC context
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Returns the MDC key for request IDs.
         *
         * @return request ID key
         */
        public String getRequestIdKey() {
            return requestIdKey;
        }

        /**
         * Sets the MDC key for request IDs.
         *
         * @param requestIdKey request ID key
         */
        public void setRequestIdKey(String requestIdKey) {
            this.requestIdKey = requestIdKey;
        }

        /**
         * Returns the MDC key for user IDs.
         *
         * @return user ID key
         */
        public String getUserIdKey() {
            return userIdKey;
        }

        /**
         * Sets the MDC key for user IDs.
         *
         * @param userIdKey user ID key
         */
        public void setUserIdKey(String userIdKey) {
            this.userIdKey = userIdKey;
        }

        /**
         * Returns whether to include client IP in MDC context.
         *
         * @return true if client IP should be included
         */
        public boolean isIncludeClientIp() {
            return includeClientIp;
        }

        /**
         * Sets whether to include client IP in MDC context.
         *
         * @param includeClientIp true to include client IP
         */
        public void setIncludeClientIp(boolean includeClientIp) {
            this.includeClientIp = includeClientIp;
        }

        /**
         * Returns whether to include user roles in MDC context.
         *
         * @return true if user roles should be included
         */
        public boolean isIncludeUserRoles() {
            return includeUserRoles;
        }

        /**
         * Sets whether to include user roles in MDC context.
         *
         * @param includeUserRoles true to include user roles
         */
        public void setIncludeUserRoles(boolean includeUserRoles) {
            this.includeUserRoles = includeUserRoles;
        }
    }


    public static class JsonLoggingProperties {
        private boolean enabled = true;
        private boolean prettyPrint = false;
        private boolean includeLoggerName = true;
        private boolean includeThreadName = true;
        private boolean includeStacktrace = true;

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isPrettyPrint() {
            return prettyPrint;
        }

        public void setPrettyPrint(boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
        }

        public boolean isIncludeLoggerName() {
            return includeLoggerName;
        }

        public void setIncludeLoggerName(boolean includeLoggerName) {
            this.includeLoggerName = includeLoggerName;
        }

        public boolean isIncludeThreadName() {
            return includeThreadName;
        }

        public void setIncludeThreadName(boolean includeThreadName) {
            this.includeThreadName = includeThreadName;
        }

        public boolean isIncludeStacktrace() {
            return includeStacktrace;
        }

        public void setIncludeStacktrace(boolean includeStacktrace) {
            this.includeStacktrace = includeStacktrace;
        }
    }
}