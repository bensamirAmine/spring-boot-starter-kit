package com.bensamir.starter.security.jwt.filter;

import com.bensamir.starter.properties.StarterKitProperties;
import com.bensamir.starter.security.jwt.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Filter that handles JWT authentication for requests.
 * <p>
 * This filter extracts and validates JWT tokens from requests,
 * and sets up Spring Security authentication.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final String tokenHeader;
    private final String tokenPrefix;

    /**
     * Creates a new JWT authentication filter.
     *
     * @param tokenService the JWT token service
     * @param userDetailsService the user details service
     * @param properties the starter kit properties
     */
    public JwtAuthenticationFilter(
            JwtTokenService tokenService,
            UserDetailsService userDetailsService,
            StarterKitProperties properties) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.tokenHeader = properties.getSecurity().getJwt().getHeaderName();
        this.tokenPrefix = properties.getSecurity().getJwt().getTokenPrefix();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                String username = tokenService.getUsernameFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Set authentication for user: {}", username);
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the request.
     *
     * @param request the HTTP request
     * @return the JWT token, or null if not found
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length()).trim();
        }
        return null;
    }
}