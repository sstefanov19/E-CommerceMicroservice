package org.example.userservice.service;


import lombok.RequiredArgsConstructor;
import org.example.userservice.models.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(!user.getUsername().equals(username)) {
            throw new UsernameNotFoundException("User isn't found in the database");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthority(user));
        }

    private Collection<? extends GrantedAuthority> getAuthority(User  user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getUsername());
        return List.of(authority);
    }
}