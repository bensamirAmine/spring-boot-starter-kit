package com.bensamir.starter.apidocs.config;

import com.bensamir.starter.properties.StarterKitProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Configures OpenAPI documentation for the application.
 * <p>
 * This configuration creates an OpenAPI specification based on properties defined
 * in the application configuration, including:
 * <ul>
 *   <li>API information (title, description, version)</li>
 *   <li>Contact information</li>
 *   <li>License information</li>
 *   <li>Server definitions</li>
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    private final StarterKitProperties properties;

    /**
     * Creates a new OpenAPI configuration.
     *
     * @param properties the starter kit properties
     */
    public OpenApiConfig(StarterKitProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates an OpenAPI bean with configuration from properties.
     *
     * @return the configured OpenAPI bean
     */
    @Bean
    public OpenAPI openAPI() {
        // Get API documentation properties
        var apiDocsProps = properties.getApiDocs();

        // Create API info
        Info info = new Info()
                .title(apiDocsProps.getTitle())
                .description(apiDocsProps.getDescription())
                .version(apiDocsProps.getVersion())
                .termsOfService(apiDocsProps.getTermsOfServiceUrl());

        // Add contact information if provided
        if (isNotEmpty(apiDocsProps.getContact().getName()) ||
                isNotEmpty(apiDocsProps.getContact().getEmail()) ||
                isNotEmpty(apiDocsProps.getContact().getUrl())) {
            info.contact(new Contact()
                    .name(apiDocsProps.getContact().getName())
                    .url(apiDocsProps.getContact().getUrl())
                    .email(apiDocsProps.getContact().getEmail()));
        }

        // Add license information if provided
        if (isNotEmpty(apiDocsProps.getLicense().getName()) ||
                isNotEmpty(apiDocsProps.getLicense().getUrl())) {
            info.license(new License()
                    .name(apiDocsProps.getLicense().getName())
                    .url(apiDocsProps.getLicense().getUrl()));
        }

        // Create OpenAPI object with info
        OpenAPI openAPI = new OpenAPI().info(info);

        // Add servers if provided
        if (!apiDocsProps.getServers().isEmpty()) {
            List<Server> servers = apiDocsProps.getServers().stream()
                    .map(server -> new Server()
                            .url(server.getUrl())
                            .description(server.getDescription()))
                    .collect(Collectors.toList());
            openAPI.servers(servers);
        }

        return openAPI;
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param str the string to check
     * @return true if the string is not null and not empty
     */
    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}