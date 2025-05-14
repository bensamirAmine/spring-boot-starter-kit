package com.bensamir.starter.security.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.core.service.DummySecurityUserService;
import com.bensamir.starter.security.core.service.SecurityUserService;
import com.bensamir.starter.security.jwt.filter.JwtAuthenticationFilter;
import com.bensamir.starter.security.jwt.service.JwtTokenService;
import com.bensamir.starter.security.web.config.WebSecurityConfig;
import com.bensamir.starter.security.web.controller.AuthController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Auto-configuration for security module.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityAutoConfiguration {

    /**
     * Password encoder bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security user service bean.
     * This will be replaced by the application's implementation.
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityUserService securityUserService(PasswordEncoder passwordEncoder) {
        return new DummySecurityUserService(passwordEncoder);
    }

    /**
     * JWT token service bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtTokenService jwtTokenService(StarterKitProperties properties) {
        return new JwtTokenService(properties);
    }

    /**
     * JWT authentication filter bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenService jwtTokenService,
            SecurityUserService securityUserService,
            StarterKitProperties properties) {
        return new JwtAuthenticationFilter(jwtTokenService, securityUserService, properties);
    }

    /**
     * Web security configuration bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public WebSecurityConfig webSecurityConfig(
            StarterKitProperties properties,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        return new WebSecurityConfig(properties, jwtAuthenticationFilter);
    }

    /**
     * Authentication manager bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Authentication controller bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthController authController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {
        return new AuthController(authenticationManager, jwtTokenService);
    }
}