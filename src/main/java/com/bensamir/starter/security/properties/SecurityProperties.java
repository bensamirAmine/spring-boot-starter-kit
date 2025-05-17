package com.bensamir.starter.security.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Security configuration properties.
 */
public class SecurityProperties {
    private boolean enabled = true;
    private final JwtProperties jwt = new JwtProperties();
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

    public AuthProperties getAuth() {
        return auth;
    }

    /**
     * JWT configuration properties.
     */
    public static class JwtProperties {
        private String secretKey = "defaultSecretKeyThatShouldBeChangedInProduction";
        private long tokenExpirationMs = 3600000; // 1 hour
        private String issuer = "spring-boot-starter-kit";
        private String tokenPrefix = "Bearer ";
        private String headerName = "Authorization";

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public long getTokenExpirationMs() {
            return tokenExpirationMs;
        }

        public void setTokenExpirationMs(long tokenExpirationMs) {
            this.tokenExpirationMs = tokenExpirationMs;
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

    /**
     * Authentication properties.
     */
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
        }

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