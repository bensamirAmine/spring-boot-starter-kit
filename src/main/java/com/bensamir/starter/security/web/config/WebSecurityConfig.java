package com.bensamir.starter.security.web.config;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.jwt.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security web configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final StarterKitProperties properties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(StarterKitProperties properties,
                             JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.properties = properties;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Get security properties
        boolean csrfEnabled = properties.getSecurity().getAuth().isCsrfEnabled();
        boolean stateless = properties.getSecurity().getAuth().isStateless();
        String[] publicPaths = properties.getSecurity().getAuth().getPublicPaths()
                .toArray(new String[0]);

        // Configure HTTP security
        http
                // CSRF configuration
                .csrf(csrf -> {
                    if (!csrfEnabled) {
                        csrf.disable();
                    }
                })

                // CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Session management
                .sessionManagement(session -> {
                    if (stateless) {
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })

                // Request authorization
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicPaths).permitAll()
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Get CORS properties
        List<String> allowedOrigins = properties.getSecurity().getCors().getAllowedOrigins();
        List<String> allowedMethods = properties.getSecurity().getCors().getAllowedMethods();
        List<String> allowedHeaders = properties.getSecurity().getCors().getAllowedHeaders();
        boolean allowCredentials = properties.getSecurity().getCors().isAllowCredentials();
        long maxAge = properties.getSecurity().getCors().getMaxAge();

        // Configure CORS
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}