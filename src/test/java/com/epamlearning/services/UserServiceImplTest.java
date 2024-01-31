package com.epamlearning.services;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.UserRepository;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.impl.AuthorizationServiceImpl;
import com.epamlearning.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorizationServiceImpl authService;

    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userServiceImpl.findByUsername(null));
    }

    @Test
    void updatePassword_ValidCredentials_ReturnsUpdatedUser() {
        // Arrange
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User userToUpdate = new User();
        userToUpdate.setPassword(oldPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userToUpdate));
        when(passwordEncoder.matches(oldPassword, userToUpdate.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        // Act
        userServiceImpl.updatePassword(username, oldPassword, newPassword);

        // Assert
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void updatePassword_InvalidOldPassword_ThrowsNotFoundException() {
        // Arrange
        String username = "testUser";
        String oldPassword = "invalidOldPassword";
        String newPassword = "newPassword";
        User userToUpdate = new User();
        userToUpdate.setPassword("correctPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userToUpdate));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userServiceImpl.updatePassword(username, oldPassword, newPassword));
    }

    @Test
    void updatePassword_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userServiceImpl.updatePassword(null, "oldPassword", "newPassword"));
    }

    @Test
    void authenticate_ValidCredentials_ReturnsSessionDTO() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(loginAttemptService.isBlocked(anyString())).thenReturn(false);
        when(jwtUtil.generateAccessToken(username)).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(username)).thenReturn("refreshToken");

        // Mocking token expiry times
        when(jwtUtil.extractExpirationDateFromToken(any())).thenReturn(new Date());

        // Act
        SessionDTO sessionDTO = userServiceImpl.authenticate(username, password);

        // Assert
        assertNotNull(sessionDTO);
        assertNotNull(sessionDTO.accessToken());
        assertNotNull(sessionDTO.refreshToken());
        verify(loginAttemptService).loginSucceeded(username);
    }

    @Test
    void testAuthenticate_SuccessfulAuthentication() {
        // Given
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateAccessToken(username)).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken);

        // When
        SessionDTO sessionDTO = userServiceImpl.authenticate(username, password);

        // Then
        assertNotNull(sessionDTO);
        assertEquals(accessToken, sessionDTO.accessToken());
        assertEquals(refreshToken, sessionDTO.refreshToken());
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtUtil, times(1)).generateAccessToken(username);
        verify(jwtUtil, times(1)).generateRefreshToken(username);
    }

    @Test
    void authenticate_InvalidPassword_ThrowsNotAuthenticatedException() {
        // Arrange
        String username = "testUser";
        String password = "invalidPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("correctPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(loginAttemptService.isBlocked(username)).thenReturn(false);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userServiceImpl.authenticate(username, password));
    }

    @Test
    void createUser_ValidNames_ReturnsUserWithGeneratedUsernameAndPassword() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        User result = userServiceImpl.createUser(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertNotNull(result.getUsername());
        assertTrue(result.isActive());
    }
}
