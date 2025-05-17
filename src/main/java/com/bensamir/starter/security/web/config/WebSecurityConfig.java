package com.bensamir.starter.security.web.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.jwt.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security web configuration.
 * <p>
 * This configuration:
 * <ul>
 *   <li>Secures endpoints with JWT authentication</li>
 *   <li>Configures public paths that don't require authentication</li>
 *   <li>Enables method-level security</li>
 * </ul>
 * <p>
 * You can override this configuration by defining your own SecurityFilterChain bean.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final StarterKitProperties properties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Creates a new WebSecurityConfig.
     *
     * @param properties the starter kit properties
     * @param jwtAuthenticationFilter the JWT authentication filter
     */
    public WebSecurityConfig(StarterKitProperties properties,
                             JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.properties = properties;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the security filter chain.
     *
     * @param http the HTTP security
     * @return the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Get security properties
        boolean csrfEnabled = properties.getSecurity().getAuth().isCsrfEnabled();
        boolean stateless = properties.getSecurity().getAuth().isStateless();
        String[] publicPaths = properties.getSecurity().getAuth().getPublicPaths()
                .toArray(new String[0]);

        // Configure HTTP security
        http.csrf(csrf -> {
                    if (!csrfEnabled) {
                        csrf.disable();
                    }
                })
                .sessionManagement(session -> {
                    if (stateless) {
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicPaths).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}