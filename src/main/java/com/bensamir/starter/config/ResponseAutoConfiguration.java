package com.bensamir.starter.config;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.response", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResponseAutoConfiguration {
    // Nothing to configure here - these are utility classes
    // The auto-configuration ensures the feature can be conditionally enabled/disabled
}