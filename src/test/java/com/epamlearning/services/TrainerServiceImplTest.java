package com.epamlearning.services;

import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.User;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.repositories.TrainerRepository;
import com.epamlearning.services.impl.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeServiceImpl traineeServiceImpl;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private AuthorizationServiceImpl authService;

    @Mock
    private TrainingTypeServiceImpl trainingTypeServiceImpl;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private TrainerServiceImpl trainerServiceImpl;

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
    void findAll_ReturnsListOfTraineeProfileResponseDTOs() {
        // Arrange
        List<TrainerProfileResponseDTO> expectedTrainers = Arrays.asList(new TrainerProfileResponseDTO(), new TrainerProfileResponseDTO());
        List<Trainer> foundTrainer = List.of(new Trainer(), new Trainer());
        when(trainerRepository.findAll()).thenReturn(foundTrainer);
        when(trainerMapper.trainersToTrainerProfileResponseDTOs(trainerRepository.findAll())).thenReturn(expectedTrainers);

        // Act
        List<TrainerProfileResponseDTO> result = trainerServiceImpl.findAll();

        // Assert
        assertEquals(expectedTrainers, result);
    }

    @Test
    void findByUsername_ValidUsername_ReturnsTraineeProfileResponseDTO() {
        // Arrange
        String username = "sampleUsername";
        TrainerProfileResponseDTO trainerProfileResponseDTO = trainerMapper.trainerToTrainerProfileResponseDTO(sampleTrainer);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        when(trainerMapper.trainerToTrainerProfileResponseDTO(sampleTrainer)).thenReturn(trainerProfileResponseDTO);

        // Act
        TrainerProfileResponseDTO result = trainerServiceImpl.findByUsername(username);

        // Assert
        assertEquals(trainerProfileResponseDTO, result);
    }

    @Test
    void findByUsername_NullUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerServiceImpl.findByUsername(null));
    }

    @Test
    void findByUsername_EmptyUsername_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerServiceImpl.findByUsername(""));
    }

    @Test
    void findByUsername_NonExistentUsername_ThrowsNotFoundException() {
        // Arrange
        String nonExistentUsername = "nonExistentUsername";
        when(trainerRepository.findTrainerByUserUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerServiceImpl.findByUsername(nonExistentUsername));
    }

    @Test
    void update_ValidData_ReturnsUpdatedTrainer() {
        // Arrange
        String username = "sampleUsername";
        TrainerUpdateRequestDTO updatedTrainer = new TrainerUpdateRequestDTO();
        updatedTrainer.setTrainingTypeName(String.valueOf(TrainingTypeName.GYM_TYPE));
        updatedTrainer.setFirstName("Updated John");
        updatedTrainer.setLastName("Updated Doe");
        updatedTrainer.setActive(false);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        sampleTrainer.setSpecialization(trainingType);
        User user = new User();
        user.setUsername("sampleUsername");
        user.setFirstName("Updated John");
        user.setLastName("Updated Doe");
        user.setActive(false);
        sampleTrainer.setUser(user);

        TrainerProfileResponseDTO traineeProfileResponseDTO = trainerMapper.trainerToTrainerProfileResponseDTO(sampleTrainer);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(sampleTrainer);
        when(trainerMapper.trainerToTrainerProfileResponseDTO(sampleTrainer)).thenReturn(traineeProfileResponseDTO);
        // Act
        TrainerProfileResponseDTO result = trainerServiceImpl.update(username, updatedTrainer);

        // Assert
        assertEquals(traineeProfileResponseDTO, result);
    }

    @Test
    void update_NonExistentUsername_ThrowsNotFoundException() {
        // Arrange
        String nonExistentUsername = "nonExistentUsername";
        when(trainerRepository.findTrainerByUserUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerServiceImpl.update(nonExistentUsername, new TrainerUpdateRequestDTO()));
    }

    @Test
    void update_NullUsername_ThrowsNullPointerException() {
        String username = null;
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerServiceImpl.update(username, new TrainerUpdateRequestDTO()));
    }


    @Test
    void update_NullTrainee_ThrowsNullPointerException() {
        String username = sampleTrainer.getUser().getUsername();
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerServiceImpl.update(username, null));
    }

    @Test
    void updateActive_ValidData_ReturnsUpdatedTrainer() {
        // Arrange
        String username = sampleTrainer.getUser().getUsername();
        TrainerProfileResponseDTO trainerProfileResponseDTO = new TrainerProfileResponseDTO();
        trainerProfileResponseDTO.setActive(false);
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(sampleTrainer);
        when(trainerMapper.trainerToTrainerProfileResponseDTO(sampleTrainer)).thenReturn(new TrainerProfileResponseDTO());
        // Act
        TrainerProfileResponseDTO result = trainerServiceImpl.updateActive(username, false);

        // Assert
        assertFalse(result.isActive());
    }

    @Test
    void updateActive_NonExistentUsername_ThrowsNotFoundException() {
        // Arrange
        String nonExistentUsername = "nonExistentUsername";
        when(trainerRepository.findTrainerByUserUsername(nonExistentUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerServiceImpl.updateActive(nonExistentUsername, false));
    }

    @Test
    void updateActive_NullUsername_ThrowsNullPointerException() {
        String username = null;
        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(Optional.of(sampleTrainer));
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainerServiceImpl.updateActive(username, false));
    }

    @Test
    void findNotAssignedActiveTrainers_ValidUsername_ReturnsListOfTrainers() {
        // Arrange
        String username = "traineeUsername";
        when(traineeServiceImpl.findByValidatedUsername(username)).thenReturn(new Trainee());

        // Act
        List<TrainerListResponseDTO> result = trainerServiceImpl.findNotAssignedActiveTrainers(username);

        // Assert
        assertNotNull(result);
    }

    @Test
    void createTrainer_ValidData_ReturnsTrainer() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        TrainerRegistrationResponseDTO trainerRegistrationResponseDTO = new TrainerRegistrationResponseDTO();
        trainerRegistrationResponseDTO.setUsername("sampleUsername");
        when(userServiceImpl.createUser(firstName, lastName)).thenReturn(sampleTrainer.getUser());
        when(trainerRepository.save(any(Trainer.class))).thenReturn(sampleTrainer);
        when(trainingTypeServiceImpl.findById(1L)).thenReturn(trainingType);
        when(trainerMapper.trainerToTrainerRegistrationResponseDTO(sampleTrainer)).thenReturn(trainerRegistrationResponseDTO);
        // Act
        TrainerRegistrationResponseDTO result = trainerServiceImpl.createTrainer(firstName, lastName,1L);

        // Assert
        assertNotNull(result);
        assertEquals("sampleUsername", result.getUsername());
    }
}