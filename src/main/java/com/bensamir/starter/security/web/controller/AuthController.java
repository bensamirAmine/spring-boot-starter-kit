package com.bensamir.starter.security.web.controller;

import com.bensamir.starter.response.ApiResponse;
import com.bensamir.starter.response.ResponseEntityBuilder;
import com.bensamir.starter.security.core.model.UserPrincipal;
import com.bensamir.starter.security.jwt.model.JwtToken;
import com.bensamir.starter.security.jwt.service.JwtTokenService;
import com.bensamir.starter.security.web.model.LoginRequest;
import com.bensamir.starter.security.web.model.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * Login endpoint.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtToken>> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String accessToken = tokenService.generateAccessToken(authentication);
        String refreshToken = tokenService.generateRefreshToken(authentication);

        // Create and return token response
        JwtToken tokenResponse = new JwtToken(
                accessToken,
                refreshToken,
                tokenService.getExpirationDateFromToken(accessToken).getTime()
        );

        return ResponseEntityBuilder.success(tokenResponse);
    }

    /**
     * Token refresh endpoint.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtToken>> refreshToken(@RequestBody RefreshTokenRequest refreshRequest) {
        // Validate refresh token
        if (!tokenService.validateToken(refreshRequest.getRefreshToken())) {
            return ResponseEntityBuilder.badRequest("INVALID_REFRESH_TOKEN", "Invalid refresh token");
        }

        // Get user ID from refresh token
        String userId = tokenService.getUserIdFromToken(refreshRequest.getRefreshToken());

        // Create authentication with user principal
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());

        // Generate new tokens
        String accessToken = tokenService.generateAccessToken(authentication);
        String refreshToken = tokenService.generateRefreshToken(authentication);

        // Create and return token response
        JwtToken tokenResponse = new JwtToken(
                accessToken,
                refreshToken,
                tokenService.getExpirationDateFromToken(accessToken).getTime()
        );

        return ResponseEntityBuilder.success(tokenResponse);
    }
}