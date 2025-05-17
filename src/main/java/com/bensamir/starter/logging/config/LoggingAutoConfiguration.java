package com.bensamir.starter.logging.config;

import com.bensamir.starter.logging.filter.MdcFilter;
import com.bensamir.starter.logging.filter.RequestLoggingFilter;
import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Auto-configuration for enterprise-grade logging infrastructure.
 * <p>
 * This configuration provides:
 * <ul>
 *   <li>MDC context for request correlation and tracing</li>
 *   <li>Detailed request/response logging</li>
 *   <li>Thread context propagation utilities</li>
 * </ul>
 * <p>
 * All components are configurable via properties:
 * <pre>
 * starter-kit:
 *   logging:
 *     enabled: true
 *     mdc:
 *       enabled: true
 *       request-id-key: "requestId"
 *       user-id-key: "userId"
 *       include-client-ip: true
 *       include-user-roles: false
 *     request:
 *       enabled: true
 *       include-headers: true
 *       include-payload: true
 *       max-payload-length: 10000
 *       exclude-paths: ["/actuator/**", "/swagger-ui/**"]
 * </pre>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "starter-kit.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    /**
     * Creates an MDC filter for request context tracking.
     * <p>
     * This filter is registered with the highest precedence to ensure
     * MDC context is available for all subsequent filters.
     *
     * @param properties the starter kit properties
     * @return a filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "starter-kit.logging.mdc", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<MdcFilter> mdcFilter(StarterKitProperties properties) {
        FilterRegistrationBean<MdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MdcFilter(properties));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    /**
     * Creates a request logging filter.
     * <p>
     * This filter is registered with a low precedence to ensure it can
     * capture the complete request/response cycle, including any modifications
     * made by intermediate filters.
     *
     * @param properties the starter kit properties
     * @return a filter registration bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "starter-kit.logging.request", name = "enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter(StarterKitProperties properties) {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggingFilter(properties));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return registrationBean;
    }
}