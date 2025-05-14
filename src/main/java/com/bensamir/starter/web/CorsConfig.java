package com.bensamir.starter.web;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

public class CorsConfig {

    private CorsConfig() {
        // Utility class, no instantiation
    }

    public static CorsFilter corsFilter(StarterKitProperties properties) {
        StarterKitProperties.WebConfigProperties.CorsProperties corsProps =
                properties.getWebConfig().getCors();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(corsProps.getAllowedOrigins()));
        config.setAllowedMethods(Arrays.asList(corsProps.getAllowedMethods()));
        config.setAllowedHeaders(Arrays.asList(corsProps.getAllowedHeaders()));
        config.setAllowCredentials(corsProps.isAllowCredentials());
        config.setMaxAge(corsProps.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}