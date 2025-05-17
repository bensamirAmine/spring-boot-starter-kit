package com.bensamir.starter.apidocs.config;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration for API documentation.
 * <p>
 * This configuration provides:
 * <ul>
 *   <li>OpenAPI configuration based on properties</li>
 *   <li>Default API grouping</li>
 * </ul>
 * <p>
 * Configuration properties:
 * <pre>
 * starter-kit:
 *   api-docs:
 *     enabled: true
 *     title: "API Documentation"
 *     description: "API Documentation"
 *     version: "1.0"
 *     contact:
 *       name: "Contact Name"
 *       email: "contact@example.com"
 *       url: "https://example.com"
 *     license:
 *       name: "MIT License"
 *       url: "https://opensource.org/licenses/MIT"
 *     servers:
 *       - url: "http://localhost:8080"
 *         description: "Local Development Server"
 * </pre>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(GroupedOpenApi.class)
@ConditionalOnProperty(prefix = "starter-kit.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(OpenApiConfig.class)
public class ApiDocsAutoConfiguration {

    /**
     * Creates a default API group for all endpoints.
     *
     * @return the default API group
     */
    @Bean
    @ConditionalOnMissingBean(name = "defaultApi")
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .build();
    }
}