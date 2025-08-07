package com.bensamir.starter.config;

import com.bensamir.starter.apidocs.config.ApiDocsAutoConfiguration;
import com.bensamir.starter.logging.config.LoggingAutoConfiguration;
import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Main auto-configuration class for the Spring Boot Starter Kit.
 * <p>
 * This starter provides essential enterprise-grade components for Spring Boot applications
 * with minimal configuration overhead:
 * <p>
 * <ul>
 *     <li><strong>Exception handling</strong>: Standardized error responses with consistent formatting</li>
 *     <li><strong>Persistence support</strong>: Entity auditing and base entity classes</li>
 *     <li><strong>API responses</strong>: Unified response format with builders for various scenarios</li>
 *     <li><strong>Web configuration</strong>: CORS and essential web settings</li>
 *     <li><strong>API documentation</strong>: OpenAPI/Swagger configuration</li>
 *     <li><strong>Enterprise logging</strong>: Request tracing and MDC context</li>
 * </ul>
 * <p>
 * All components can be selectively enabled or disabled via configuration properties.
 * Security is intentionally excluded to allow applications to implement their own
 * authentication and authorization strategies.
 * <p>
 * Example configuration:
 * <pre>
 * starter-kit:
 *   exception-handling:
 *     enabled: true
 *   logging:
 *     enabled: true
 *   api-docs:
 *     enabled: true
 * </pre>
 *
 * @see com.bensamir.starter.properties.StarterKitProperties
 */
@Configuration
@EnableConfigurationProperties(StarterKitProperties.class)
@Import({
        ExceptionHandlingAutoConfiguration.class,
        PersistenceAutoConfiguration.class,
        ResponseAutoConfiguration.class,
        WebConfigAutoConfiguration.class,
        ApiDocsAutoConfiguration.class,
        LoggingAutoConfiguration.class
})
public class StarterKitAutoConfiguration {
    // Main entry point for auto-configuration
    // Security components intentionally excluded for flexibility
}