package com.bensamir.starter.web;

import com.bensamir.starter.properties.StarterKitProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration for Spring MVC.
 * <p>
 * This class provides essential configurations for Spring MVC applications:
 * <ul>
 *   <li>JSON message conversion with appropriate date/time handling</li>
 *   <li>Character encoding for requests and responses</li>
 *   <li>Other common web MVC settings</li>
 * </ul>
 */
public class WebMvcConfig implements WebMvcConfigurer {

    private final StarterKitProperties properties;

    /**
     * Creates a new WebMvcConfig.
     *
     * @param properties The starter kit properties
     */
    public WebMvcConfig(StarterKitProperties properties) {
        this.properties = properties;
    }

    /**
     * Configures the HTTP message converters.
     * <p>
     * This method adds a Jackson converter with appropriate configuration
     * for JSON serialization/deserialization.
     *
     * @param converters The list of converters to add to
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Configure Jackson with common settings
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        // Add our converter at index 0 to ensure it takes precedence
        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}