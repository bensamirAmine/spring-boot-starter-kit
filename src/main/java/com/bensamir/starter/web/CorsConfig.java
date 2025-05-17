package com.bensamir.starter.web;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Configuration for Cross-Origin Resource Sharing (CORS).
 * <p>
 * This class provides CORS configuration based on properties defined in the
 * application configuration. It enables fine-grained control over:
 * <ul>
 *   <li>Allowed origins</li>
 *   <li>Allowed HTTP methods</li>
 *   <li>Allowed headers</li>
 *   <li>Exposed headers</li>
 *   <li>Credentials support</li>
 *   <li>Preflight request caching</li>
 * </ul>
 */
public final class CorsConfig {

    private CorsConfig() {
        // Utility class, no instantiation
    }

    /**
     * Creates a CORS filter based on the provided properties.
     *
     * @param properties The starter kit properties
     * @return A configured CORS filter
     */
    public static CorsFilter corsFilter(StarterKitProperties properties) {
        StarterKitProperties.WebConfigProperties.CorsProperties corsProps =
                properties.getWebConfig().getCors();

        CorsConfiguration config = new CorsConfiguration();

        // Configure allowed origins
        if (corsProps.getAllowedOrigins() != null) {
            config.setAllowedOrigins(Arrays.asList(corsProps.getAllowedOrigins()));
        }

        // Configure allowed methods
        if (corsProps.getAllowedMethods() != null) {
            config.setAllowedMethods(Arrays.asList(corsProps.getAllowedMethods()));
        }

        // Configure allowed headers
        if (corsProps.getAllowedHeaders() != null) {
            config.setAllowedHeaders(Arrays.asList(corsProps.getAllowedHeaders()));
        }

        // Configure credentials and max age
        config.setAllowCredentials(corsProps.isAllowCredentials());
        config.setMaxAge(corsProps.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}