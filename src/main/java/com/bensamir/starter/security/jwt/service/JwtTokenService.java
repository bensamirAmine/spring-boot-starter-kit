package com.bensamir.starter.security.jwt.service;

import com.bensamir.starter.properties.StarterKitProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Core service for JWT token operations.
 * <p>
 * This service handles token generation, validation, and claims extraction
 * with proper security and error handling.
 */
public class JwtTokenService {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    private final SecretKey secretKey;
    private final long tokenExpirationMs;
    private final String issuer;

    /**
     * Creates a new JWT token service.
     *
     * @param properties the starter kit properties
     */
    public JwtTokenService(StarterKitProperties properties) {
        String secret = properties.getSecurity().getJwt().getSecretKey();
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenExpirationMs = properties.getSecurity().getJwt().getTokenExpirationMs();
        this.issuer = properties.getSecurity().getJwt().getIssuer();
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param userDetails the user details
     * @return the JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Add authorities if present
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if (!authorities.isEmpty()) {
            claims.put("authorities", authorities);
        }

        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Generates a JWT token with custom claims.
     *
     * @param claims the custom claims
     * @param subject the token subject (usually username)
     * @return the JWT token
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer(issuer)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates a token.
     *
     * @param token the JWT token
     * @return true if the token is valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }

    /**
     * Gets the username from a token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Gets all claims from a token.
     *
     * @param token the JWT token
     * @return the claims
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}