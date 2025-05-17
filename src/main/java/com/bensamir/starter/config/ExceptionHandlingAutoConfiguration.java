package com.bensamir.starter.config;

import com.bensamir.starter.exception.ErrorMessageResolver;
import com.bensamir.starter.exception.GlobalExceptionHandler;
import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Auto-configuration for standardized exception handling.
 * <p>
 * This configuration provides:
 * <ul>
 *   <li>Global exception handling with consistent error responses</li>
 *   <li>Support for internationalized error messages</li>
 *   <li>Mapping of common exceptions to appropriate HTTP status codes</li>
 * </ul>
 * <p>
 * Configuration properties:
 * <pre>
 * starter-kit:
 *   exception-handling:
 *     enabled: true                        # Enable/disable exception handling
 *     log-exceptions: true                 # Log exceptions
 *     include-stack-trace: false           # Include stack traces in responses (not for production)
 *     enable-i18n: false                   # Enable internationalization of error messages
 * </pre>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.exception-handling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlingAutoConfiguration {

    /**
     * Creates a message source for error messages if not already defined.
     * <p>
     * This message source looks for messages in the "messages/error-messages" resource bundle.
     *
     * @return the message source
     */
    @Bean
    @ConditionalOnMissingBean(name = "exceptionMessageSource")
    public MessageSource exceptionMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/error-messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    /**
     * Creates an ErrorMessageResolver if not already defined.
     * <p>
     * The resolver handles internationalization and default message resolution.
     *
     * @param messageSource the message source
     * @param properties the starter kit properties
     * @return the error message resolver
     */
    @Bean
    @ConditionalOnMissingBean
    public ErrorMessageResolver errorMessageResolver(MessageSource messageSource,
                                                     StarterKitProperties properties) {
        return new ErrorMessageResolver(messageSource, properties);
    }

    /**
     * Creates a GlobalExceptionHandler if not already defined.
     * <p>
     * The handler maps exceptions to appropriate HTTP responses.
     *
     * @param properties the starter kit properties
     * @param messageResolver the error message resolver
     * @return the global exception handler
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(StarterKitProperties properties,
                                                         ErrorMessageResolver messageResolver) {
        return new GlobalExceptionHandler(properties, messageResolver);
    }
}