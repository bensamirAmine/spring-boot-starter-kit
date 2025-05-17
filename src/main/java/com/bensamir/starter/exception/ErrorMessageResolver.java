package com.bensamir.starter.exception;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;

/**
 * Resolves error messages from i18n message sources or default messages.
 * <p>
 * This resolver supports:
 * <ul>
 *   <li>Internationalized messages from a MessageSource</li>
 *   <li>Fallback to configured default messages</li>
 *   <li>Final fallback to provided default messages</li>
 * </ul>
 */
public class ErrorMessageResolver {
    private final MessageSource messageSource;
    private final StarterKitProperties properties;
    private final boolean i18nEnabled;

    /**
     * Creates a new ErrorMessageResolver.
     *
     * @param messageSource the message source for internationalized messages
     * @param properties the starter kit properties
     */
    public ErrorMessageResolver(MessageSource messageSource, StarterKitProperties properties) {
        this.messageSource = messageSource;
        this.properties = properties;
        this.i18nEnabled = properties.getExceptionHandling().isEnableI18n();
    }

    /**
     * Resolves an error message using the configured resolution strategy.
     *
     * @param errorCode the error code to resolve
     * @param defaultMessage the fallback default message
     * @param args message arguments for placeholders
     * @return the resolved message
     */
    public String resolveMessage(String errorCode, String defaultMessage, Object... args) {
        // If i18n is disabled, use default message or configured message
        if (!i18nEnabled) {
            String configuredMessage = properties.getExceptionHandling().getDefaultMessages().get(errorCode);
            if (configuredMessage != null) {
                // Apply formatting if there are any placeholder arguments
                if (args != null && args.length > 0) {
                    return MessageFormat.format(configuredMessage, args);
                }
                return configuredMessage;
            }
            return defaultMessage;
        }

        // Try to get localized message
        try {
            return messageSource.getMessage(errorCode, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            // If not found, fall back to configured default
            String configuredMessage = properties.getExceptionHandling().getDefaultMessages().get(errorCode);
            if (configuredMessage != null) {
                // Apply formatting if there are any placeholder arguments
                if (args != null && args.length > 0) {
                    return MessageFormat.format(configuredMessage, args);
                }
                return configuredMessage;
            }
            return defaultMessage;
        }
    }
}