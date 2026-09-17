package com.habotconnect.security;

import com.habotconnect.entity.User;
import com.habotconnect.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Loads users (by email) for Spring Security's authentication machinery.
 * The email is used as the "username" throughout.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                u.email,
                u.password,
                List.of(new SimpleGrantedAuthority("ROLE_" + u.role.name()))
        );
    }
}
