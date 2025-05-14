package com.bensamir.starter.exception;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

public class ErrorMessageResolver {
    private final MessageSource messageSource;
    private final StarterKitProperties properties;
    private final boolean i18nEnabled;

    public ErrorMessageResolver(MessageSource messageSource, StarterKitProperties properties) {
        this.messageSource = messageSource;
        this.properties = properties;
        this.i18nEnabled = properties.getExceptionHandling().isEnableI18n();
    }

    public String resolveMessage(String errorCode, String defaultMessage, Object... args) {
        // If i18n is disabled, use default message or configured message
        if (!i18nEnabled) {
            String configuredMessage = properties.getExceptionHandling().getDefaultMessages().get(errorCode);
            return configuredMessage != null ? configuredMessage : defaultMessage;
        }

        // Try to get localized message
        try {
            return messageSource.getMessage(errorCode, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            // If not found, fall back to configured default
            String configuredMessage = properties.getExceptionHandling().getDefaultMessages().get(errorCode);
            return configuredMessage != null ? configuredMessage : defaultMessage;
        }
    }
}