package com.bensamir.starter.controller.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auto-configuration for base controllers.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({RestController.class, JpaRepository.class})
@ConditionalOnProperty(prefix = "starter-kit.controllers", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ControllersAutoConfiguration {
    // No beans to register - just make available the base controller classes
}