package com.bensamir.starter.config;

import com.bensamir.starter.persistence.AuditingConfig;
import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@ConditionalOnClass({ JpaRepository.class })
@ConditionalOnProperty(prefix = "starter-kit.base-entity", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PersistenceAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "starter-kit.base-entity", name = "enable-auditing", havingValue = "true", matchIfMissing = true)
    @Import(AuditingConfig.class)
    public static class AuditingConfiguration {
        // Import the auditing configuration
    }
}