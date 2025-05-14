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

    public static class RequestLoggingProperties {
        private boolean enabled = true;
        private boolean includeHeaders = true;
        private boolean includePayload = true;
        private boolean includeQueryString = true;
        private int maxPayloadLength = 10000;
        private List<String> excludePaths = new ArrayList<>();

        public RequestLoggingProperties() {
            // Default exclude paths
            excludePaths.add("/actuator/**");
            excludePaths.add("/swagger-ui/**");
            excludePaths.add("/v3/api-docs/**");
        }

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isIncludeHeaders() {
            return includeHeaders;
        }

        public void setIncludeHeaders(boolean includeHeaders) {
            this.includeHeaders = includeHeaders;
        }

        public boolean isIncludePayload() {
            return includePayload;
        }

        public void setIncludePayload(boolean includePayload) {
            this.includePayload = includePayload;
        }

        public boolean isIncludeQueryString() {
            return includeQueryString;
        }

        public void setIncludeQueryString(boolean includeQueryString) {
            this.includeQueryString = includeQueryString;
        }

        public int getMaxPayloadLength() {
            return maxPayloadLength;
        }

        public void setMaxPayloadLength(int maxPayloadLength) {
            this.maxPayloadLength = maxPayloadLength;
        }

        public List<String> getExcludePaths() {
            return excludePaths;
        }

        public void setExcludePaths(List<String> excludePaths) {
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

    public static class MdcProperties {
        private boolean enabled = true;
        private String requestIdKey = "requestId";
        private String userIdKey = "userId";
        private boolean includeIpAddress = true;
        private String ipAddressKey = "clientIp";

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getRequestIdKey() {
            return requestIdKey;
        }

        public void setRequestIdKey(String requestIdKey) {
            this.requestIdKey = requestIdKey;
        }

        public String getUserIdKey() {
            return userIdKey;
        }

        public void setUserIdKey(String userIdKey) {
            this.userIdKey = userIdKey;
        }

        public boolean isIncludeIpAddress() {
            return includeIpAddress;
        }

        public void setIncludeIpAddress(boolean includeIpAddress) {
            this.includeIpAddress = includeIpAddress;
        }

        public String getIpAddressKey() {
            return ipAddressKey;
        }

        public void setIpAddressKey(String ipAddressKey) {
            this.ipAddressKey = ipAddressKey;
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