package com.bensamir.starter.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Auto-configuration for standardized API responses.
 * <p>
 * This configuration enables the standardized response format
 * with appropriate JSON serialization settings.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.response", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResponseAutoConfiguration {

    /**
     * Configures Jackson ObjectMapper with appropriate settings for API responses.
     *
     * @return The configured ObjectMapper
     */
    @Bean
    @ConditionalOnMissingBean(name = "responseObjectMapper")
    public ObjectMapper responseObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}