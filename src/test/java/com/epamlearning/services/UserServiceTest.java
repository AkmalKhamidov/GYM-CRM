package com.epamlearning.services;

import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ValidId_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findById(userId);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    void findById_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.findById(null));
    }

    @Test
    void findByUsername_ValidUsername_ReturnsUser() {
        // Arrange
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertEquals(expectedUser, result);
    }

    @Test
    void findByUsername_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.findByUsername(null));
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        // Arrange
        User userToSave = new User();
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        // Act
        User result = userService.save(userToSave);

        // Assert
        assertEquals(userToSave, result);
    }

    @Test
    void update_ValidIdAndUser_ReturnsUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User userToUpdate = new User();
        User updatedUser = new User();
        userToUpdate.setFirstName("TestUserFirstName");
        userToUpdate.setLastName("TestUserLastName");
        userToUpdate.setActive(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.update(userId, userToUpdate);

        // Assert
        assertEquals(updatedUser, result);
    }

    @Test
    void update_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.update(null, new User()));
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
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        // Act
        User result = userService.updatePassword(username, oldPassword, newPassword);

        // Assert
        assertEquals(userToUpdate, result);
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
        assertThrows(NotFoundException.class, () -> userService.updatePassword(username, oldPassword, newPassword));
    }

    @Test
    void updatePassword_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.updatePassword(null, "oldPassword", "newPassword"));
    }

    @Test
    void updateActive_ValidIdAndActive_ReturnsUpdatedUser() {
        // Arrange
        Long userId = 1L;
        boolean active = true;
        User userToUpdate = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        // Act
        User result = userService.updateActive(userId, active);

        // Assert
        assertEquals(userToUpdate, result);
    }

    @Test
    void updateActive_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.updateActive(null, true));
    }

    @Test
    void findAll_NoArguments_ReturnsListOfUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertEquals(expectedUsers, result);
    }

    @Test
    void deleteById_ValidId_DeletesUser() {
        // Arrange
        Long userId = 1L;
        User userToDelete = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        // Act
        userService.deleteById(userId);

        // Assert
        verify(userRepository, times(1)).delete(userToDelete);
    }

    @Test
    void deleteById_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.deleteById(null));
    }

    @Test
    void authenticate_ValidCredentials_ReturnsUserId() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Long result = userService.authenticate(username, password);

        // Assert
        assertEquals(user.getId(), result);
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

        // Act & Assert
        assertThrows(NotAuthenticated.class, () -> userService.authenticate(username, password));
    }

    @Test
    void createUser_ValidNames_ReturnsUserWithGeneratedUsernameAndPassword() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        User result = userService.createUser(firstName, lastName);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
        assertTrue(result.isActive());
    }
}
