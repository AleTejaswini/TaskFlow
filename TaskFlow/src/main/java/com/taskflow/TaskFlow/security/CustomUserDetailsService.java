package com.taskflow.TaskFlow.security;

import com.taskflow.TaskFlow.entity.User;
import com.taskflow.TaskFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Spring Security requires roles to be prefixed with "ROLE_"
        String role = user.getRole().startsWith("ROLE_")
            ? user.getRole()
            : "ROLE_" + user.getRole();

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(role)
            .build();
    }
}
