package com.epamlearning.services;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.User;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer sampleTrainer;

    @BeforeEach
    void setUp() {
        sampleTrainer = new Trainer();
        sampleTrainer.setId(1L);

        User sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("sampleUsername");
        sampleUser.setPassword("samplePassword");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setActive(true);

        TrainingType sampleTrainingType = new TrainingType();
        sampleTrainingType.setId(1L);
        sampleTrainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);

        sampleTrainer.setUser(sampleUser);
        sampleTrainer.setSpecialization(sampleTrainingType);
    }

    @Test
    void findById_ValidId_ReturnsTrainer() {
        // Arrange
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(sampleTrainer));

        // Act
        Trainer result = trainerService.findById(1L);

        // Assert
        assertEquals(sampleTrainer, result);
    }

    // Similar tests for findById_NullId, findById_NonExistentId, findAll, findByUsername, etc.

    @Test
    void save_ValidTrainer_ReturnsSavedTrainer() {
        // Arrange
        when(trainerRepository.save(sampleTrainer)).thenReturn(sampleTrainer);

        // Act
        Trainer result = trainerService.save(sampleTrainer);

        // Assert
        assertEquals(sampleTrainer, result);
    }

    @Test
    void update_ValidData_ReturnsUpdatedTrainer() {
        // Arrange
        Long trainerId = 1L;
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(trainerId);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUsername");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setFirstName("Updated John");
        updatedUser.setLastName("Updated Doe");
        updatedUser.setActive(false);

        TrainingType updatedTrainingType = new TrainingType();
        updatedTrainingType.setId(2L);
        updatedTrainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE_2);

        updatedTrainer.setUser(updatedUser);
        updatedTrainer.setSpecialization(updatedTrainingType);

        when(trainerRepository.save(any(Trainer.class))).thenReturn(updatedTrainer);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(sampleTrainer));
        when(userService.userNullVerification(updatedUser)).thenReturn(true);

        // Act
        Trainer result = trainerService.update(trainerId, updatedTrainer);

        // Assert
        assertEquals(updatedTrainer, result);
        assertEquals(updatedUser, result.getUser());
        assertEquals(updatedTrainingType, result.getSpecialization());
    }

    @Test
    void update_NullTrainer_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerService.update(1L, null));
    }

    @Test
    void authenticate_ValidCredentials_ReturnsTrainerId() {
        // Arrange
        String username = "sampleUsername";
        String password = "samplePassword";
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        when(userService.authenticate(username, password)).thenReturn(sampleTrainer.getId());

        // Act
        Long result = trainerService.authenticate(username, password);

        // Assert
        assertEquals(sampleTrainer.getId(), result);
    }

    @Test
    void authenticate_InvalidPassword_ThrowsNotAuthenticatedException() {
        // Arrange
        String username = "sampleUsername";
        String password = "invalidPassword";
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        when(userService.authenticate(username, password)).thenReturn(null);

        // Act & Assert
        assertThrows(NotAuthenticated.class, () -> trainerService.authenticate(username, password));
    }

    @Test
    void updateActive_ValidData_ReturnsUpdatedTrainer() {
        // Arrange
        Long trainerId = 1L;
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(sampleTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(sampleTrainer);
        // Act
        Trainer result = trainerService.updateActive(trainerId, false);

        // Assert
        assertFalse(result.getUser().isActive());
    }

    @Test
    void updatePassword_ValidData_ReturnsUpdatedTrainer() {
        // Arrange
        Long trainerId = 1L;
        String newPassword = "newPassword";
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(sampleTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(sampleTrainer);

        // Act
        Trainer result = trainerService.updatePassword(trainerId, newPassword);

        // Assert
        assertEquals(newPassword, result.getUser().getPassword());
    }

    @Test
    void findNotAssignedActiveTrainers_ValidUsername_ReturnsListOfTrainers() {
        // Arrange
        String username = "traineeUsername";
        when(traineeService.findByUsername(username)).thenReturn(new Trainee()); // Assuming trainee exists

        // Act
        List<Trainer> result = trainerService.findNotAssignedActiveTrainers(username);

        // Assert
        assertNotNull(result);
    }

    @Test
    void createTrainer_ValidData_ReturnsTrainer() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Long trainingTypeId = 1L;
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        when(userService.createUser(firstName, lastName)).thenReturn(user);
        when(trainingTypeService.findById(trainingTypeId)).thenReturn(new TrainingType());

        // Act
        Trainer result = trainerService.createTrainer(firstName, lastName, trainingTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(firstName, result.getUser().getFirstName());
        assertEquals(lastName, result.getUser().getLastName());
        assertNotNull(result.getSpecialization());
    }
}