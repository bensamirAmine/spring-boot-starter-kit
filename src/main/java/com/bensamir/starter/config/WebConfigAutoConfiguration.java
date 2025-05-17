package com.bensamir.starter.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.web.CompressionConfig;
import com.bensamir.starter.web.CorsConfig;
import com.bensamir.starter.web.WebMvcConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Auto-configuration for web configuration components.
 * <p>
 * This configuration provides:
 * <ul>
 *   <li>CORS configuration for cross-origin requests</li>
 *   <li>Response compression for improved performance</li>
 *   <li>Common web MVC settings</li>
 * </ul>
 * <p>
 * Configuration properties:
 * <pre>
 * starter-kit:
 *   web-config:
 *     enabled: true                     # Enable/disable web configuration
 *     cors:
 *       enabled: true                   # Enable/disable CORS
 *       allowed-origins: ["*"]          # Allowed origins
 *       allowed-methods: ["GET", ...]   # Allowed methods
 *     compression:
 *       enabled: true                   # Enable/disable compression
 *       min-response-size: 2048         # Minimum size to compress
 * </pre>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.web-config", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebConfigAutoConfiguration {

    /**
     * Creates a CORS filter if CORS is enabled.
     *
     * @param properties The starter kit properties
     * @return A filter registration bean for the CORS filter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "starter-kit.web-config.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<CorsFilter> corsFilter(StarterKitProperties properties) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(CorsConfig.corsFilter(properties));
        registration.setOrder(0);
        return registration;
    }

    /**
     * Creates a WebMvcConfig bean.
     *
     * @param properties The starter kit properties
     * @return The WebMvcConfig bean
     */
    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfig webMvcConfig(StarterKitProperties properties) {
        return new WebMvcConfig(properties);
    }

    /**
     * Creates a CompressionConfig bean if compression is enabled.
     *
     * @param properties The starter kit properties
     * @return The CompressionConfig bean
     */
    @Bean
    @ConditionalOnClass(ConfigurableServletWebServerFactory.class)
    @ConditionalOnProperty(prefix = "starter-kit.web-config.compression", name = "enabled", havingValue = "true", matchIfMissing = true)
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> compressionCustomizer(
            StarterKitProperties properties) {
        return new CompressionConfig(properties);
    }

    /**
     * Creates a servlet customizer to enable the default servlet.
     *
     * @return The servlet customizer
     */
    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> servletCustomizer() {
        return factory -> factory.setRegisterDefaultServlet(true);
    }
}