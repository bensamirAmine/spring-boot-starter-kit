// src/main/java/com/bensamir/starter/logging/config/LoggingAutoConfiguration.java
package com.bensamir.starter.logging.config;

import com.bensamir.starter.logging.aspect.LogExecutionAspect;
import com.bensamir.starter.logging.aspect.PerformanceLoggingAspect;
import com.bensamir.starter.logging.filter.MdcFilter;
import com.bensamir.starter.logging.filter.RequestResponseLoggingFilter;
import com.bensamir.starter.properties.StarterKitProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * Auto-configuration for logging.
 */
@Configuration
@ConditionalOnProperty(prefix = "starter-kit.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    private final StarterKitProperties properties;
    private final Environment environment;

    public LoggingAutoConfiguration(StarterKitProperties properties, Environment environment) {
        this.properties = properties;
        this.environment = environment;
    }

    /**
     * Initializes logging configuration by setting system properties.
     */
    @PostConstruct
    public void initLoggingConfig() {
        // Set application name for logging
        System.setProperty("LOG_APP_NAME",
                environment.getProperty("spring.application.name", "application"));

        // Set JSON logging properties
        System.setProperty("LOG_INCLUDE_LOGGER_NAME",
                String.valueOf(properties.getLogging().getJson().isIncludeLoggerName()));
        System.setProperty("LOG_INCLUDE_THREAD_NAME",
                String.valueOf(properties.getLogging().getJson().isIncludeThreadName()));
        System.setProperty("LOG_INCLUDE_STACKTRACE",
                String.valueOf(properties.getLogging().getJson().isIncludeStacktrace()));

        // Configure pretty print via system property
        System.setProperty("LOG_PRETTY_PRINT",
                String.valueOf(properties.getLogging().getJson().isPrettyPrint()));
    }

    /**
     * MDC filter registration.
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(prefix = "starter-kit.logging.mdc", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<MdcFilter> mdcFilter() {
        FilterRegistrationBean<MdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MdcFilter(properties));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    /**
     * Request-response logging filter registration.
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(prefix = "starter-kit.logging.request", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseLoggingFilter() {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseLoggingFilter(properties));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return registrationBean;
    }

    /**
     * Performance logging aspect.
     */
    @Bean
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    @ConditionalOnProperty(prefix = "starter-kit.logging.performance", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public PerformanceLoggingAspect performanceLoggingAspect() {
        return new PerformanceLoggingAspect(properties);
    }

    /**
     * Log execution aspect.
     */
    @Bean
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    @ConditionalOnMissingBean
    public LogExecutionAspect logExecutionAspect() {
        return new LogExecutionAspect();
    }
}