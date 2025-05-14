package com.bensamir.starter.apidocs.config;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Auto-configuration for API documentation.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(GroupedOpenApi.class)
@ConditionalOnProperty(prefix = "starter-kit.api-docs", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(OpenApiConfig.class)
public class ApiDocsAutoConfiguration {

    /**
     * Default API group for all endpoints.
     */
    @Bean
    @ConditionalOnMissingBean(name = "defaultApi")
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * API group for public endpoints.
     */
    @Bean
    @ConditionalOnMissingBean(name = "publicApi")
    public GroupedOpenApi publicApi(StarterKitProperties properties) {
        // Get public paths from security properties
        String[] publicPaths = properties.getSecurity().getAuth().getPublicPaths()
                .toArray(new String[0]);

        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(publicPaths)
                .build();
    }
}