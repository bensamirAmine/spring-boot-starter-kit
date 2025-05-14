package com.bensamir.starter.security.web.model;

/**
 * Refresh token request model.
 */
public class RefreshTokenRequest {

    private String refreshToken;

    // Getters and setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}