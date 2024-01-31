package com.epamlearning.services.impl;

import com.epamlearning.exceptions.NotAuthorized;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl {
    public void authorizeUser(String username) {
        // Retrieve the currently logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Check if the logged-in user is authorized to update the profile
        if (!loggedInUsername.equals(username)) {
            throw new NotAuthorized("Access is denied");
        }
    }
}
