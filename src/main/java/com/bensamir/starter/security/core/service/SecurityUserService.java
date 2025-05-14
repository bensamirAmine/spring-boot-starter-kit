package com.bensamir.starter.security.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface for security user services.
 */
public interface SecurityUserService extends UserDetailsService {

    /**
     * Load a user by their ID.
     *
     * @param id The user ID
     * @return The UserDetails for the user
     * @throws UsernameNotFoundException If the user cannot be found
     */
    UserDetails loadUserById(String id) throws UsernameNotFoundException;
}