package com.epamlearning.services;

import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee sampleTrainee;

    @BeforeEach
    void setUp() {
        sampleTrainee = new Trainee();
        sampleTrainee.setId(1L);
        sampleTrainee.setDateOfBirth(new Date());
        sampleTrainee.setAddress("Sample Address");

        User sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("sampleUsername");
        sampleUser.setPassword("samplePassword");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setActive(true);

        sampleTrainee.setUser(sampleUser);
    }

    @Test
    void findById_ValidId_ReturnsTrainee() {
        // Arrange
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(sampleTrainee));

        // Act
        Trainee result = traineeService.findById(1L);

        // Assert
        assertEquals(sampleTrainee, result);
    }

    @Test
    void findById_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeService.findById(null));
    }

    @Test
    void findById_NonExistentId_ThrowsNotFoundException() {
        // Arrange
        when(traineeRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> traineeService.findById(2L));
    }

    @Test
    void findAll_ReturnsListOfTrainees() {
        // Arrange
        List<Trainee> expectedTrainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeRepository.findAll()).thenReturn(expectedTrainees);

        // Act
        List<Trainee> result = traineeService.findAll();

        // Assert
        assertEquals(expectedTrainees, result);
    }

    @Test
    void findByUsername_ValidUsername_ReturnsTrainee() {
        // Arrange
        String username = "sampleUsername";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));

        // Act
        Trainee result = traineeService.findByUsername(username);

        // Assert
        assertEquals(sampleTrainee, result);
    }

    @Test
    void findByUsername_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeService.findByUsername(null));
    }

    @Test
    void findByUsername_EmptyUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeService.findByUsername(""));
    }

    @Test
    void findByUsername_NonExistentUsername_ThrowsNotFoundException() {
        // Arrange
        String nonExistentUsername = "nonExistentUsername";
        when(traineeRepository.findTraineeByUserUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> traineeService.findByUsername(nonExistentUsername));
    }

    @Test
    void save_ValidTrainee_ReturnsSavedTrainee() {
        // Arrange
        when(traineeRepository.save(sampleTrainee)).thenReturn(sampleTrainee);

        // Act
        Trainee result = traineeService.save(sampleTrainee);

        // Assert
        assertEquals(sampleTrainee, result);
    }

    @Test
    void update_ValidData_ReturnsUpdatedTrainee() {
        // Arrange
        Long traineeId = 1L;
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(traineeId);
        updatedTrainee.setDateOfBirth(new Date());
        updatedTrainee.setAddress("Updated Address");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUsername");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setFirstName("Updated John");
        updatedUser.setLastName("Updated Doe");
        updatedUser.setActive(false);

        updatedTrainee.setUser(updatedUser);

        when(traineeRepository.save(any(Trainee.class))).thenReturn(updatedTrainee);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(sampleTrainee));
        when(userService.userNullVerification(updatedUser)).thenReturn(true);

        // Act
        Trainee result = traineeService.update(traineeId, updatedTrainee);

        // Assert
        assertEquals(updatedTrainee, result);
        assertEquals(updatedUser, result.getUser());
    }

    @Test
    void update_NullTrainee_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> traineeService.update(1L, null));
    }

    @Test
    void authenticate_ValidCredentials_ReturnsTraineeId() {
        // Arrange
        String username = "sampleUsername";
        String password = "samplePassword";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));
        when(userService.authenticate(username, password)).thenReturn(sampleTrainee.getId());

        // Act
        Long result = traineeService.authenticate(username, password);

        // Assert
        assertEquals(sampleTrainee.getId(), result);
    }

    @Test
    void authenticate_InvalidPassword_ThrowsNotAuthenticatedException() {
        // Arrange
        String username = "sampleUsername";
        String password = "invalidPassword";
        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(Optional.of(sampleTrainee));
        when(userService.authenticate(username, password)).thenReturn(null);

        // Act & Assert
        assertThrows(NotAuthenticated.class, () -> traineeService.authenticate(username, password));
    }

    @Test
    void hasTrainer_TraineeHasTrainer_ReturnsTrue() {
        // Arrange
        Long trainerId = 1L;
        User user = new User();
        Trainer trainer = new Trainer();
        trainer.setId(trainerId);
        user.setUsername("TrainerUserName");
        user.setUsername("TrainerFirstName");
        user.setUsername("TrainerLastName");
        trainer.setUser(user);
        sampleTrainee.getTrainers().add(trainer);
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(sampleTrainee));
        // Act
        boolean result = traineeService.hasTrainer(sampleTrainee.getId(), trainerId);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasTrainer_TraineeDoesNotHaveTrainer_ReturnsFalse() {
        // Arrange
        Long trainerId = 1L;
        when(traineeRepository.findById(any(Long.class))).thenReturn(Optional.of(sampleTrainee));

        // Act
        boolean result = traineeService.hasTrainer(sampleTrainee.getId(), trainerId);

        // Assert
        assertFalse(result);
    }

    @Test
    void createTrainee_ValidData_ReturnsTrainee() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String address = "Sample Address";
        Date dateOfBirth = new Date();
        when(userService.createUser(firstName, lastName)).thenReturn(sampleTrainee.getUser());

        // Act
        Trainee result = traineeService.createTrainee(firstName, lastName, address, dateOfBirth);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getUser().getFirstName());
        assertEquals(lastName, result.getUser().getLastName());
        assertEquals(address, result.getAddress());
        assertEquals(dateOfBirth, result.getDateOfBirth());
    }
}
