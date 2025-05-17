package com.bensamir.starter.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;

/**
 * Configuration for JPA entity auditing.
 * <p>
 * This configuration enables:
 * <ul>
 *   <li>Automatic tracking of creation and modification timestamps</li>
 *   <li>Automatic tracking of created-by and modified-by user information</li>
 *   <li>Consistent date/time handling with UTC timestamps</li>
 * </ul>
 * <p>
 * When a Spring Security authenticated user is available, the username will be used
 * for the created-by and modified-by fields. Otherwise, they will be null.
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider", auditorAwareRef = "auditorProvider")
public class AuditingConfig {

    /**
     * Creates a DateTimeProvider that uses UTC timestamps for auditing.
     *
     * @return the date time provider
     */
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

    /**
     * Creates an AuditorAware that extracts the current user from the security context.
     *
     * @return the auditor aware
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.empty();
            }

            // Use the authenticated user's name as the auditor
            return Optional.of(authentication.getName());
        };
    }
}