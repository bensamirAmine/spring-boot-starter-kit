package com.bensamir.starter.properties;

import com.bensamir.starter.apidocs.properties.ApiDocsProperties;
import com.bensamir.starter.controller.properties.ControllersProperties;
import com.bensamir.starter.logging.properties.LoggingProperties;
import com.bensamir.starter.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for the Spring Boot Starter Kit.
 * These properties allow fine-grained control over all components
 * provided by the starter.
 *
 * Properties can be configured in application.yml or application.properties:
 * <pre>
 * starter-kit:
 *   exception-handling:
 *     enabled: true
 *   base-entity:
 *     enabled: true
 *   # ... and so on
 * </pre>
 *
 * @author Ben Samir
 */
@ConfigurationProperties(prefix = "starter-kit")
public class StarterKitProperties {

    private final ExceptionHandlingProperties exceptionHandling = new ExceptionHandlingProperties();
    private final BaseEntityProperties baseEntity = new BaseEntityProperties();
    private final WebConfigProperties webConfig = new WebConfigProperties();
    private final ResponseProperties response = new ResponseProperties();
    private final SecurityProperties security = new SecurityProperties();
    private final ApiDocsProperties apiDocs = new ApiDocsProperties();
    private final LoggingProperties logging = new LoggingProperties();
    private final ControllersProperties controllers = new ControllersProperties();


    public ExceptionHandlingProperties getExceptionHandling() {
        return exceptionHandling;
    }

    public BaseEntityProperties getBaseEntity() {
        return baseEntity;
    }

    public WebConfigProperties getWebConfig() {
        return webConfig;
    }

    public ResponseProperties getResponse() {
        return response;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public ApiDocsProperties getApiDocs() {
        return apiDocs;
    }

    public LoggingProperties getLogging() {
        return logging;
    }

    public ControllersProperties getControllers() {
        return controllers;
    }


    public static class ExceptionHandlingProperties {
        private boolean enabled = true;
        private boolean logExceptions = true;
        private boolean includeStackTrace = false;
        private boolean enableI18n = false;
        private Map<String, String> defaultMessages = new HashMap<>();

        public ExceptionHandlingProperties() {
            defaultMessages.put("resource.notfound", "Resource not found");
            defaultMessages.put("bad.request", "Bad request");
            defaultMessages.put("validation.error", "Validation error");
            defaultMessages.put("internal.error", "An unexpected error occurred");
        }

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

    public static class BaseEntityProperties {

        private boolean enabled = true;
        private boolean enableAuditing = true;
        private String idType = "LONG";

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

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }
    }

    public static class WebConfigProperties {
        private boolean enabled = true;
        private final CorsProperties cors = new CorsProperties();
        private final CompressionProperties compression = new CompressionProperties();

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

        public static class CompressionProperties {
            private boolean enabled = true;
            private DataSize minResponseSize = DataSize.ofKilobytes(2);
            private String[] mimeTypes = {
                    "text/html", "text/xml", "text/plain", "text/css",
                    "text/javascript", "application/javascript", "application/json"
            };

            // Getters and setters
            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public DataSize getMinResponseSize() {
                return minResponseSize;
            }

            public void setMinResponseSize(DataSize minResponseSize) {
                this.minResponseSize = minResponseSize;
            }

            public String[] getMimeTypes() {
                return mimeTypes;
            }

            public void setMimeTypes(String[] mimeTypes) {
                this.mimeTypes = mimeTypes;
            }
        }
    }

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
}