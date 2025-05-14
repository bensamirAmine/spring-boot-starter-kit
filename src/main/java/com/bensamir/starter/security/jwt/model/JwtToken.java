package com.bensamir.starter.security.jwt.model;

/**
 * Represents a JWT token pair (access and refresh tokens).
 */
public class JwtToken {

    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;

    public JwtToken(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}