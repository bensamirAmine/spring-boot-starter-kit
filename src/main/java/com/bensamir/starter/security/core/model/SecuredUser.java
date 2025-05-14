package com.bensamir.starter.security.core.model;

import java.util.Set;

/**
 * Interface for user entities that can be used with the security module.
 * Implementing this interface allows your user entity to work with the security components.
 */
public interface SecuredUser {

    /**
     * Gets the user's unique identifier.
     */
    String getId();

    /**
     * Gets the user's username or email.
     */
    String getUsername();

    /**
     * Gets the user's password (hashed).
     */
    String getPassword();

    /**
     * Gets the user's roles.
     */
    Set<String> getRoles();

    /**
     * Checks if the user's account is enabled.
     */
    boolean isEnabled();
}