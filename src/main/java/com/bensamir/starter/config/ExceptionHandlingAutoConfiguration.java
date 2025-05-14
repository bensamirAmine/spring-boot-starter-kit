package com.bensamir.starter.config;

import com.bensamir.starter.exception.ErrorMessageResolver;
import com.bensamir.starter.exception.GlobalExceptionHandler;
import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.exception-handling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ExceptionHandlingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MessageSource exceptionMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/error-messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorMessageResolver errorMessageResolver(MessageSource messageSource,
                                                     StarterKitProperties properties) {
        return new ErrorMessageResolver(messageSource, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(StarterKitProperties properties,
                                                         ErrorMessageResolver messageResolver) {
        return new GlobalExceptionHandler(properties, messageResolver);
    }
}