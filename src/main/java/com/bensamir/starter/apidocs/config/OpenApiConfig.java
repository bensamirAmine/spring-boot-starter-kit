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
 * OpenAPI (Swagger) configuration.
 */
@Configuration
public class OpenApiConfig {

    private final StarterKitProperties properties;

    public OpenApiConfig(StarterKitProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI openAPI() {
        // Get API documentation properties
        var apiDocsProps = properties.getApiDocs();

        // Create API info
        Info info = new Info()
                .title(apiDocsProps.getTitle())
                .description(apiDocsProps.getDescription())
                .version(apiDocsProps.getVersion())
                .termsOfService(apiDocsProps.getTermsOfServiceUrl())
                .contact(new Contact()
                        .name(apiDocsProps.getContact().getName())
                        .url(apiDocsProps.getContact().getUrl())
                        .email(apiDocsProps.getContact().getEmail()))
                .license(new License()
                        .name(apiDocsProps.getLicense().getName())
                        .url(apiDocsProps.getLicense().getUrl()));

        // Create server list
        List<Server> servers = apiDocsProps.getServers().stream()
                .map(server -> new Server()
                        .url(server.getUrl())
                        .description(server.getDescription()))
                .collect(Collectors.toList());

        // Create and return OpenAPI object
        return new OpenAPI()
                .info(info)
                .servers(servers);
    }
}