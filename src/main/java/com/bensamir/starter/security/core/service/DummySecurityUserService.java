package com.bensamir.starter.security.core.service;

import com.bensamir.starter.security.core.model.SecurityRole;
import com.bensamir.starter.security.core.model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dummy implementation of SecurityUserService for testing.
 * This should not be used in production.
 */
public class DummySecurityUserService implements SecurityUserService {

    private final Map<String, UserDetails> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public DummySecurityUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Add default users
        addUser("1", "user", "password", Set.of(SecurityRole.USER));
        addUser("2", "admin", "admin", Set.of(SecurityRole.ADMIN, SecurityRole.USER));
    }

    private void addUser(String id, String username, String password, Set<String> roles) {
        UserPrincipal user = UserPrincipal.from(new DummyUser(
                id,
                username,
                passwordEncoder.encode(password),
                roles,
                true
        ));
        users.put(id, user);
        users.put(username, user); // For loadUserByUsername
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    @Override
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        UserDetails user = users.get(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        return user;
    }

    private static class DummyUser implements com.bensamir.starter.security.core.model.SecuredUser {
        private final String id;
        private final String username;
        private final String password;
        private final Set<String> roles;
        private final boolean enabled;

        public DummyUser(String id, String username, String password, Set<String> roles, boolean enabled) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.roles = roles;
            this.enabled = enabled;
        }

        @Override
        public String getUserId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public Set<String> getRoles() {
            return roles;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}