package com.bensamir.starter.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.web.CorsConfig;
import com.bensamir.starter.web.WebMvcConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;


import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.web-config", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebConfigAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "starter-kit.web-config.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<CorsFilter> corsFilter(StarterKitProperties properties) {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(CorsConfig.corsFilter(properties));
        registration.setOrder(0);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfig webMvcConfig(StarterKitProperties properties) {
        return new WebMvcConfig(properties);
    }

    @Bean
    @ConditionalOnClass(ConfigurableServletWebServerFactory.class)
    @ConditionalOnProperty(prefix = "starter-kit.web-config.compression", name = "enabled", havingValue = "true", matchIfMissing = true)
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> serverFactoryCustomizer(
            StarterKitProperties properties) {

        return factory -> {
            StarterKitProperties.WebConfigProperties.CompressionProperties compressionProps =
                    properties.getWebConfig().getCompression();

            if (compressionProps.isEnabled()) {
                Compression compression = new Compression();
                compression.setEnabled(true);
                compression.setMinResponseSize(compressionProps.getMinResponseSize());
                compression.setMimeTypes(compressionProps.getMimeTypes());
                factory.setCompression(compression);
            }
        };
    }

    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> servletCustomizer() {
        return factory -> {
            factory.setRegisterDefaultServlet(true);
            // Add more servlet customizations if needed
        };
    }
}