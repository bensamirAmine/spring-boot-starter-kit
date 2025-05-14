package com.bensamir.starter.security.properties;

import java.util.ArrayList;
import java.util.List;

public class SecurityProperties {
    private boolean enabled = true;
    private final JwtProperties jwt = new JwtProperties();
    private final CorsProperties cors = new CorsProperties();
    private final AuthProperties auth = new AuthProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public JwtProperties getJwt() {
        return jwt;
    }

    public CorsProperties getCors() {
        return cors;
    }

    public AuthProperties getAuth() {
        return auth;
    }

    public static class JwtProperties {
        private String secretKey = "defaultSecretKeyThatShouldBeChangedInProduction";
        private long accessTokenExpirationMs = 900000; // 15 minutes
        private long refreshTokenExpirationMs = 2592000000L; // 30 days
        private String issuer = "spring-boot-starter-kit";
        private String tokenPrefix = "Bearer ";
        private String headerName = "Authorization";

        // Getters and setters
        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public long getAccessTokenExpirationMs() {
            return accessTokenExpirationMs;
        }

        public void setAccessTokenExpirationMs(long accessTokenExpirationMs) {
            this.accessTokenExpirationMs = accessTokenExpirationMs;
        }

        public long getRefreshTokenExpirationMs() {
            return refreshTokenExpirationMs;
        }

        public void setRefreshTokenExpirationMs(long refreshTokenExpirationMs) {
            this.refreshTokenExpirationMs = refreshTokenExpirationMs;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getTokenPrefix() {
            return tokenPrefix;
        }

        public void setTokenPrefix(String tokenPrefix) {
            this.tokenPrefix = tokenPrefix;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
    }

    public static class CorsProperties {
        private boolean enabled = true;
        private List<String> allowedOrigins = new ArrayList<>();
        private List<String> allowedMethods = new ArrayList<>();
        private List<String> allowedHeaders = new ArrayList<>();
        private boolean allowCredentials = true;
        private long maxAge = 3600;

        public CorsProperties() {
            // Default values
            allowedOrigins.add("*");
            allowedMethods.add("GET");
            allowedMethods.add("POST");
            allowedMethods.add("PUT");
            allowedMethods.add("DELETE");
            allowedMethods.add("OPTIONS");
            allowedHeaders.add("*");
        }

        // Getters and setters
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
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

    public static class AuthProperties {
        private List<String> publicPaths = new ArrayList<>();
        private boolean stateless = true;
        private boolean csrfEnabled = false;

        public AuthProperties() {
            // Default public paths
            publicPaths.add("/api/auth/**");
            publicPaths.add("/api/public/**");
            publicPaths.add("/swagger-ui/**");
            publicPaths.add("/v3/api-docs/**");
            publicPaths.add("/actuator/**");
            publicPaths.add("/h2-console/**");
        }

        // Getters and setters
        public List<String> getPublicPaths() {
            return publicPaths;
        }

        public void setPublicPaths(List<String> publicPaths) {
            this.publicPaths = publicPaths;
        }

        public boolean isStateless() {
            return stateless;
        }

        public void setStateless(boolean stateless) {
            this.stateless = stateless;
        }

        public boolean isCsrfEnabled() {
            return csrfEnabled;
        }

        public void setCsrfEnabled(boolean csrfEnabled) {
            this.csrfEnabled = csrfEnabled;
        }
    }
}