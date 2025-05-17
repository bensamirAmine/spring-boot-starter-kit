package com.bensamir.starter.properties;

import com.bensamir.starter.apidocs.properties.ApiDocsProperties;
import com.bensamir.starter.security.properties.SecurityProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for the Spring Boot Starter Kit.
 * <p>
 * This class defines all configurable aspects of the starter kit components,
 * with reasonable defaults that can be overridden in application.yml or
 * application.properties.
 * <p>
 * Example configuration:
 * <pre>
 * starter-kit:
 *   exception-handling:
 *     enabled: true
 *   security:
 *     jwt:
 *       secret-key: "${JWT_SECRET:your-secret-key}"
 * </pre>
 */
@ConfigurationProperties(prefix = "starter-kit")
@Validated
public class StarterKitProperties {

    @Valid
    @NotNull
    private final ExceptionHandlingProperties exceptionHandling = new ExceptionHandlingProperties();

    @Valid
    @NotNull
    private final BaseEntityProperties baseEntity = new BaseEntityProperties();

    @Valid
    @NotNull
    private final WebConfigProperties webConfig = new WebConfigProperties();

    @Valid
    @NotNull
    private final ResponseProperties response = new ResponseProperties();

    @Valid
    @NotNull
    private final SecurityProperties security = new SecurityProperties();

    @Valid
    @NotNull
    private final ApiDocsProperties apiDocs = new ApiDocsProperties();

    @Valid
    @NotNull
    private final LoggingProperties logging = new LoggingProperties();

    /**
     * Returns the exception handling configuration properties.
     *
     * @return exception handling properties
     */
    public ExceptionHandlingProperties getExceptionHandling() {
        return exceptionHandling;
    }

    /**
     * Returns the base entity configuration properties.
     *
     * @return base entity properties
     */
    public BaseEntityProperties getBaseEntity() {
        return baseEntity;
    }

    /**
     * Returns the web configuration properties.
     *
     * @return web configuration properties
     */
    public WebConfigProperties getWebConfig() {
        return webConfig;
    }

    /**
     * Returns the response formatting properties.
     *
     * @return response properties
     */
    public ResponseProperties getResponse() {
        return response;
    }

    /**
     * Returns the security configuration properties.
     *
     * @return security properties
     */
    public SecurityProperties getSecurity() {
        return security;
    }

    /**
     * Returns the API documentation properties.
     *
     * @return API documentation properties
     */
    public ApiDocsProperties getApiDocs() {
        return apiDocs;
    }

    /**
     * Returns the logging configuration properties.
     *
     * @return logging properties
     */
    public LoggingProperties getLogging() {
        return logging;
    }

    /**
     * Exception handling configuration properties.
     */
    public static class ExceptionHandlingProperties {
        private boolean enabled = true;
        private boolean logExceptions = true;
        private boolean includeStackTrace = false;
        private boolean enableI18n = false;
        private Map<String, String> defaultMessages = new HashMap<>();

        /**
         * Initializes default error messages.
         */
        public ExceptionHandlingProperties() {
            defaultMessages.put("resource.notfound", "Resource not found");
            defaultMessages.put("bad.request", "Bad request");
            defaultMessages.put("validation.error", "Validation error");
            defaultMessages.put("internal.error", "An unexpected error occurred");
            defaultMessages.put("unauthorized", "Unauthorized");
            defaultMessages.put("forbidden", "Forbidden");
            defaultMessages.put("conflict", "Conflict");
            defaultMessages.put("missing.parameter", "Required parameter '{0}' is missing");
            defaultMessages.put("type.mismatch", "Parameter '{0}' should be of type {1}");
        }

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isLogExceptions() {
            return logExceptions;
        }

        public void setLogExceptions(boolean logExceptions) {
            this.logExceptions = logExceptions;
        }

        public boolean isIncludeStackTrace() {
            return includeStackTrace;
        }

        public void setIncludeStackTrace(boolean includeStackTrace) {
            this.includeStackTrace = includeStackTrace;
        }

        public boolean isEnableI18n() {
            return enableI18n;
        }

        public void setEnableI18n(boolean enableI18n) {
            this.enableI18n = enableI18n;
        }

        public Map<String, String> getDefaultMessages() {
            return defaultMessages;
        }

        public void setDefaultMessages(Map<String, String> defaultMessages) {
            this.defaultMessages = defaultMessages;
        }
    }

    /**
     * Base entity configuration properties.
     */
    public static class BaseEntityProperties {
        private boolean enabled = true;
        private boolean enableAuditing = true;

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnableAuditing() {
            return enableAuditing;
        }

        public void setEnableAuditing(boolean enableAuditing) {
            this.enableAuditing = enableAuditing;
        }
    }

    /**
     * Web configuration properties.
     */
    public static class WebConfigProperties {
        private boolean enabled = true;
        private final CorsProperties cors = new CorsProperties();
        private final CompressionProperties compression = new CompressionProperties();

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public CorsProperties getCors() {
            return cors;
        }

        public CompressionProperties getCompression() {
            return compression;
        }

        /**
         * CORS configuration properties.
         */
        public static class CorsProperties {
            private boolean enabled = true;
            private String[] allowedOrigins = {"*"};
            private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
            private String[] allowedHeaders = {"*"};
            private boolean allowCredentials = true;
            private long maxAge = 3600;

            // Getters and setters
            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String[] getAllowedOrigins() {
                return allowedOrigins;
            }

            public void setAllowedOrigins(String[] allowedOrigins) {
                this.allowedOrigins = allowedOrigins;
            }

            public String[] getAllowedMethods() {
                return allowedMethods;
            }

            public void setAllowedMethods(String[] allowedMethods) {
                this.allowedMethods = allowedMethods;
            }

            public String[] getAllowedHeaders() {
                return allowedHeaders;
            }

            public void setAllowedHeaders(String[] allowedHeaders) {
                this.allowedHeaders = allowedHeaders;
            }

            public boolean isAllowCredentials() {
                return allowCredentials;
            }

            public void setAllowCredentials(boolean allowCredentials) {
                this.allowCredentials = allowCredentials;
            }

            public long getMaxAge() {
                return maxAge;
            }

            public void setMaxAge(long maxAge) {
                this.maxAge = maxAge;
            }
        }

        /**
         * Response compression configuration properties.
         */
        public static class CompressionProperties {
            private boolean enabled = true;
            private String[] mimeTypes = {
                    "text/html",
                    "text/xml",
                    "text/plain",
                    "text/css",
                    "text/javascript",
                    "application/javascript",
                    "application/json",
                    "application/xml"
            };
            private int minResponseSize = 2048; // 2KB

            // Getters and setters
            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String[] getMimeTypes() {
                return mimeTypes;
            }

            public void setMimeTypes(String[] mimeTypes) {
                this.mimeTypes = mimeTypes;
            }

            public int getMinResponseSize() {
                return minResponseSize;
            }

            public void setMinResponseSize(int minResponseSize) {
                this.minResponseSize = minResponseSize;
            }
        }
    }

    /**
     * Response configuration properties.
     */
    public static class ResponseProperties {
        private boolean enabled = true;
        private boolean includeTimestamp = true;

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isIncludeTimestamp() {
            return includeTimestamp;
        }

        public void setIncludeTimestamp(boolean includeTimestamp) {
            this.includeTimestamp = includeTimestamp;
        }
    }

    /**
     * Logging configuration properties.
     */
    public static class LoggingProperties {
        private boolean enabled = true;
        private final RequestLoggingProperties request = new RequestLoggingProperties();
        private final MdcProperties mdc = new MdcProperties();

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public RequestLoggingProperties getRequest() {
            return request;
        }

        public MdcProperties getMdc() {
            return mdc;
        }

        /**
         * Request logging configuration properties.
         */
        public static class RequestLoggingProperties {
            private boolean enabled = true;
            private boolean includeHeaders = true;
            private boolean includePayload = true;
            private int maxPayloadLength = 10000;
            private String[] excludePaths = {"/actuator/**", "/swagger-ui/**", "/v3/api-docs/**"};

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

            public int getMaxPayloadLength() {
                return maxPayloadLength;
            }

            public void setMaxPayloadLength(int maxPayloadLength) {
                this.maxPayloadLength = maxPayloadLength;
            }

            public String[] getExcludePaths() {
                return excludePaths;
            }

            public void setExcludePaths(String[] excludePaths) {
                this.excludePaths = excludePaths;
            }
        }

        /**
         * MDC context configuration properties.
         */
        public static class MdcProperties {
            private boolean enabled = true;
            private String requestIdKey = "requestId";
            private String userIdKey = "userId";
            private boolean includeClientIp = true;
            private boolean includeUserRoles = false;

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

            public boolean isIncludeClientIp() {
                return includeClientIp;
            }

            public void setIncludeClientIp(boolean includeClientIp) {
                this.includeClientIp = includeClientIp;
            }

            public boolean isIncludeUserRoles() {
                return includeUserRoles;
            }

            public void setIncludeUserRoles(boolean includeUserRoles) {
                this.includeUserRoles = includeUserRoles;
            }
        }
    }
}