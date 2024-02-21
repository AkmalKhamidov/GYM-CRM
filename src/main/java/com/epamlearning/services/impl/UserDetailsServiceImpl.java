package com.epamlearning.services.impl;

import com.epamlearning.entities.User;
import com.epamlearning.repositories.UserRepository;
import com.epamlearning.security.UserDetailsAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final LoginAttemptServiceImpl loginAttemptService;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                  LoginAttemptServiceImpl loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(loginAttemptService.isBlocked(username)){
            throw new UsernameNotFoundException("User is blocked.");
        }

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserDetailsAdapter(user);
    }
}
