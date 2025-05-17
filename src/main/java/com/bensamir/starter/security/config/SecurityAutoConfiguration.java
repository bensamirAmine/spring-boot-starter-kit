package com.bensamir.starter.security.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.jwt.filter.JwtAuthenticationFilter;
import com.bensamir.starter.security.jwt.service.JwtTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Auto-configuration for JWT security components.
 * <p>
 * This configuration provides the essential components for JWT-based authentication:
 * <ul>
 *   <li>JWT token service for generating and validating tokens</li>
 *   <li>JWT authentication filter for processing token-based authentication</li>
 *   <li>Password encoder for secure password hashing</li>
 * </ul>
 * <p>
 * To use this in your application:
 * <ol>
 *   <li>Provide a UserDetailsService bean to load user data</li>
 *   <li>Configure security properties in application.yml/properties</li>
 *   <li>Create controllers for authentication as needed</li>
 * </ol>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityAutoConfiguration {

    /**
     * Creates a password encoder for secure password hashing.
     *
     * @return the password encoder
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a JWT token service.
     *
     * @param properties the starter kit properties
     * @return the JWT token service
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtTokenService jwtTokenService(StarterKitProperties properties) {
        return new JwtTokenService(properties);
    }

    /**
     * Creates a JWT authentication filter.
     * <p>
     * This bean is only created if a UserDetailsService is available.
     *
     * @param tokenService the JWT token service
     * @param userDetailsService the user details service
     * @param properties the starter kit properties
     * @return the JWT authentication filter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserDetailsService.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenService tokenService,
            UserDetailsService userDetailsService,
            StarterKitProperties properties) {
        return new JwtAuthenticationFilter(tokenService, userDetailsService, properties);
    }
}