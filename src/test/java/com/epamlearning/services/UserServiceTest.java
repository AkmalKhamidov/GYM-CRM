package com.epamlearning.services;

import com.epamlearning.daos.UserDAOImpl;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAOImpl userDAO;

    @InjectMocks
    private UserService userService;

    private User user;

//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setId(999L);
//        user.setUsername("TestFirstName.TestLastName");
//        user.setFirstName("TestFirstName");
//        user.setLastName("TestLastName");
//        user.setPassword("testPassword");
//        user.setActive(true);
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void saveUser() {
//        when(userDAO.saveOrUpdate(user)).thenReturn(Optional.of(user));
//
//        Optional<User> savedUser = userService.save(user);
//
//        assertTrue(savedUser.isPresent());
//        assertEquals(user, savedUser.get());
//    }
//
//    @Test
//    void updateUser() {
//        long userId = 999L;
//        user.setUsername("NewTestFirstName.NewTestLastName");
//        user.setFirstName("NewTestFirstName");
//        user.setLastName("NewTestLastName");
//        user.setActive(true);
//        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));
//        when(userDAO.saveOrUpdate(any(User.class))).thenReturn(Optional.of(user));
//
//        Optional<User> userUpdated = userService.update(userId, user);
//
//        assertTrue(userUpdated.isPresent());
//        assertEquals(user, userUpdated.get());
//    }
//
//    @Test
//    void updateUserPassword() {
//        long userId = 999L;
//        String newPassword = "newPassword";
//        user.setPassword(newPassword);
//        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));
//        when(userDAO.saveOrUpdate(any(User.class))).thenReturn(Optional.of(user));
//
//        Optional<User> userUpdated = userService.updatePassword(userId, newPassword);
//
//        assertTrue(userUpdated.isPresent());
//        assertEquals(user, userUpdated.get());
//    }
//
//    @Test
//    void updateActiveStatus() {
//        long userId = 999L;
//        boolean newActiveStatus = false;
//        user.setActive(false);
//        when(userDAO.findById(anyLong())).thenReturn(Optional.of(user));
//        when(userDAO.saveOrUpdate(any(User.class))).thenReturn(Optional.of(user));
//
//        Optional<User> userUpdated = userService.updateActive(userId, newActiveStatus);
//
//        assertTrue(userUpdated.isPresent());
//        assertEquals(user, userUpdated.get());
//    }
//
//    @Test
//    void findUserById() {
//        long userId = 1L;
//        User user = new User();
//        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
//
//        Optional<User> foundUser = userService.findById(userId);
//
//        assertTrue(foundUser.isPresent());
//        assertEquals(user, foundUser.get());
//    }
//
//    @Test
//    void findAllUsers() {
//        List<Optional<User>> users = new ArrayList<>();
//        when(userDAO.findAll()).thenReturn(users);
//
//        List<Optional<User>> foundUsers = userService.findAll();
//
//        assertEquals(users, foundUsers);
//    }
//
//    @Test
//    void deleteUserById() {
//        long userId = 999L;
//        when(userDAO.findById(userId)).thenReturn(Optional.of(user));
//
//        assertDoesNotThrow(() -> userService.deleteById(userId));
//        verify(userDAO, times(1)).delete(any(User.class));
//    }
//
//    @Test
//    void deleteUserByIdNotFound() {
//        long userId = 999L;
//        when(userDAO.findById(userId)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteById(userId));
//        assertEquals("User with ID " + userId + " not found for delete.", exception.getMessage());
//        verify(userDAO, never()).delete(any(User.class));
//    }
//
//    @Test
//    void findByUsername() {
//        when(userDAO.findByUserName(user.getUsername())).thenReturn(Optional.of(user));
//
//        Optional<User> foundUser = userService.findByUsername(user.getUsername());
//
//        assertTrue(foundUser.isPresent());
//        assertEquals(user, foundUser.get());
//    }
//
//    @Test
//    void findByUsernameNotFound() {
//        String username = "nonExistentUser";
//        when(userDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByUsername(username));
//        assertEquals("User with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateUser() {
//        String username = "testUser";
//        String password = "testPassword";
//        when(userDAO.findByUserName(username)).thenReturn(Optional.of(user));
//
//        assertEquals( user.getId(), userService.authenticate(username, password));
//
//    }
//
//    @Test
//    void authenticateUserNotFound() {
//        String username = "nonExistentUser";
//        String password = "testPassword";
//        when(userDAO.findByUserName(username)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.authenticate(username, password));
//        assertEquals("User with username " + username + " not found.", exception.getMessage());
//    }
//
//    @Test
//    void authenticateUserWrongPassword() {
//        String username = "testUser";
//        String correctPassword = "correctPassword";
//        String wrongPassword = "wrongPassword";
//        User user = new User();
//        user.setPassword(correctPassword);
//        when(userDAO.findByUserName(username)).thenReturn(Optional.of(user));
//
//        NotAuthenticated exception = assertThrows(NotAuthenticated.class, () -> userService.authenticate(username, wrongPassword));
//        assertEquals("Wrong password. Username: " + username, exception.getMessage());
//    }
//
//    @Test
//    void createUser() {
//        String firstName = "John";
//        String lastName = "Doe";
//        User generatedUser = userService.createUser(firstName, lastName);
//
//        assertNotNull(generatedUser);
//        assertEquals(firstName, generatedUser.getFirstName());
//        assertEquals(lastName, generatedUser.getLastName());
//        assertTrue(generatedUser.isActive());
//    }
//
//    @Test
//    void generateUserName() {
//        String firstName = "John";
//        String lastName = "Doe";
//        String generatedUsername = userService.generateUserName(firstName, lastName);
//
//        assertNotNull(generatedUsername);
//        assertTrue(generatedUsername.startsWith(firstName + "." + lastName));
//    }
//
//    @Test
//    void generateRandomPassword() {
//        String generatedPassword = userService.generateRandomPassword();
//
//        assertNotNull(generatedPassword);
//        assertEquals(10, generatedPassword.length());
//    }
}
