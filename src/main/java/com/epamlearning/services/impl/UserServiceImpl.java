package com.epamlearning.services.impl;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.ProfileDTO;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.UserRepository;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.UserService;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptServiceImpl loginAttemptService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(JWTUtil jwtUtil, AuthenticationManager authenticationManager, LoginAttemptServiceImpl loginAttemptService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.loginAttemptService = loginAttemptService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NotFoundException("Username is null.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        return user.get();
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NotFoundException("Username is null.");
        }
        if(oldPassword == null || oldPassword.isEmpty()) {
            log.warn("Old password is null.");
            throw new NotFoundException("Old password is null.");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            log.warn("New password is null.");
            throw new NotFoundException("New password is null.");
        }
        User userToUpdate = findByUsername(username);
        if(!passwordEncoder.matches(oldPassword, userToUpdate.getPassword())) {
            log.warn("Old password is wrong.");
            throw new NotFoundException("Old password is wrong.");
        }
        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToUpdate);
    }

    @Override
    public SessionDTO authenticate(String username, String password) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NotFoundException("Username is null.");
        }
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NotFoundException("Password is null.");
        }
        if(loginAttemptService.isBlocked(username)) {
            log.warn("User is blocked.");
            throw new NotAuthenticated("User is blocked.");
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptService.loginSucceeded(username);
            return generateTokens(username);
        } catch (BadCredentialsException e){
            loginAttemptService.loginFailed(username);
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong username and password");
        }
    }

    public User createUser(String firstName, String lastName) {

        if (firstName == null || firstName.isEmpty()) {
            log.warn("First name is null.");
            throw new NotFoundException("First name is null.");
        }
        if (lastName == null || lastName.isEmpty()) {
            log.warn("Last name is null.");
            throw new NotFoundException("Last name is null.");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUserName(firstName, lastName));
        user.setPassword(generateRandomPassword());
        user.setActive(true);
        return user;
    }

    public String generateUserName(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        log.info("User's username generated: {}, firstName: {}, lastName: {}", username, firstName, lastName);
        return username;
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public void userNullVerification(ProfileDTO dto) {
        if (dto == null) {
            log.warn("ProfileDTO is null.");
            throw new NotFoundException("ProfileDTO is null.");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            log.warn("ProfileDTO: First name is null.");
            throw new NotFoundException("ProfileDTO: First name is null.");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            log.warn("ProfileDTO: Last name is null.");
            throw new NotFoundException("ProfileDTO: Last name is null.");
        }
    }

    public SessionDTO generateTokens(String username) {
        // Generate JWT tokens
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // Set expiration times
        Date accessTokenExpiry = jwtUtil.extractExpirationDateFromToken(accessToken);
        Date refreshTokenExpiry = jwtUtil.extractExpirationDateFromToken(refreshToken);

        // Build and return SessionDTO
        return new SessionDTO(
                accessTokenExpiry, new Date(), accessToken, // AccessToken issued at the current time
                refreshTokenExpiry, new Date(), refreshToken // RefreshToken issued at the current time
        );
    }

}
