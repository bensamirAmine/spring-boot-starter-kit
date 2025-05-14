package com.bensamir.starter.config;

import com.bensamir.starter.apidocs.config.ApiDocsAutoConfiguration;
import com.bensamir.starter.controller.config.ControllersAutoConfiguration;
import com.bensamir.starter.logging.config.LoggingAutoConfiguration;
import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.config.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;



/**
 * Main auto-configuration class for the Spring Boot Starter Kit.
 * This starter provides commonly used components for Spring Boot applications:
 * <ul>
 *     <li>Exception handling with standardized error responses</li>
 *     <li>Base entity classes for JPA entities</li>
 *     <li>Standardized API response utilities</li>
 *     <li>Web configuration for CORS, compression, etc.</li>
 * </ul>
 *
 * All components can be enabled/disabled via configuration properties.
 *
 * @author Ben Samir
 * @see com.bensamir.starter.properties.StarterKitProperties
 */
@Configuration
@EnableConfigurationProperties(StarterKitProperties.class)
@Import({
        ExceptionHandlingAutoConfiguration.class,
        PersistenceAutoConfiguration.class,
        ResponseAutoConfiguration.class,
        WebConfigAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        ApiDocsAutoConfiguration.class,
        LoggingAutoConfiguration.class,
        ControllersAutoConfiguration.class
})
public class StarterKitAutoConfiguration {
    // Main entry point for auto-configuration
}