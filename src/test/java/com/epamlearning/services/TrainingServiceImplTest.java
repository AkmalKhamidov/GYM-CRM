package com.epamlearning.services;

import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.*;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.epamlearning.mapper.TrainingMapper;
import com.epamlearning.repositories.TrainingRepository;
import com.epamlearning.services.impl.TraineeServiceImpl;
import com.epamlearning.services.impl.TrainerServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeServiceImpl traineeServiceImpl;

    @Mock
    private TrainerServiceImpl trainerServiceImpl;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingServiceImpl;

    private Training sampleTraining;

    @BeforeEach
    void setUp() {
        User trainerUser = new User();
        User traineeUser = new User();

        trainerUser.setUsername("trainerUser");
        traineeUser.setUsername("traineeUser");

        sampleTraining = new Training();
        sampleTraining.setId(1L);

        Trainer sampleTrainer = new Trainer();
        sampleTrainer.setId(1L);
        sampleTrainer.setUser(trainerUser);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        sampleTrainer.setSpecialization(trainingType);
        sampleTraining.setTrainer(sampleTrainer);


        Trainee sampleTrainee = new Trainee();
        sampleTrainee.setId(2L);
        sampleTrainee.setUser(traineeUser);
        sampleTraining.setTrainee(sampleTrainee);

        TrainingType sampleTrainingType = new TrainingType();
        sampleTrainingType.setId(3L);
        sampleTraining.setTrainingType(sampleTrainingType);

        sampleTraining.setTrainingName("Sample Training");
        sampleTraining.setTrainingDate(LocalDate.now());
        sampleTraining.setTrainingDuration(BigDecimal.TEN);

        // Reset SecurityContextHolder before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testFindByTraineeAndCriteria() {
        // Arrange
        String traineeUsername = "trainee";
        LocalDate dateFrom = LocalDate.of(2024, 1, 1);
        LocalDate dateTo = LocalDate.of(2024, 1, 31);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        String trainerName = "trainer";

        List<Training> trainings = Collections.singletonList(new Training());
        List<TraineeTrainingsResponseDTO> expectedResponse = Collections.singletonList(new TraineeTrainingsResponseDTO());

        when(traineeServiceImpl.findByValidatedUsername(traineeUsername)).thenReturn(null);
        when(trainingRepository.findTrainingsByTraineeAndCriteria(null, dateFrom, dateTo, trainingType, trainerName)).thenReturn(trainings);
        when(trainingMapper.trainingsToTraineeTrainingsResponseDTOs(trainings)).thenReturn(expectedResponse);

        // Act
        List<TraineeTrainingsResponseDTO> actualResponse = trainingServiceImpl.findByTraineeAndCriteria(traineeUsername, dateFrom, dateTo, trainingType, trainerName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testFindByTrainerAndCriteria() {
        // Arrange
        String trainerUsername = "trainer";
        LocalDate dateFrom = LocalDate.of(2024, 1, 1);
        LocalDate dateTo = LocalDate.of(2024, 1, 31);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        String traineeName = "trainee";

        List<Training> trainings = Collections.singletonList(new Training());
        List<TrainerTrainingsResponseDTO> expectedResponse = Collections.singletonList(new TrainerTrainingsResponseDTO());

        when(trainerServiceImpl.findByValidatedUsername(trainerUsername)).thenReturn(null);
        when(trainingRepository.findTrainingsByTrainerAndCriteria(null, dateFrom, dateTo, trainingType, traineeName)).thenReturn(trainings);
        when(trainingMapper.trainingsToTrainerTrainingsResponseDTOs(trainings)).thenReturn(expectedResponse);

        // Act
        List<TrainerTrainingsResponseDTO> actualResponse = trainingServiceImpl.findByTrainerAndCriteria(trainerUsername, dateFrom, dateTo, trainingType, traineeName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void create_ValidTraining_ReturnsSavedTraining() {
        String trainingName = "Sample Training";
        LocalDate trainingDate = LocalDate.now();
        BigDecimal trainingDuration = BigDecimal.valueOf(30);
        String traineeUsername = sampleTraining.getTrainee().getUser().getUsername();
        String trainerUsername = sampleTraining.getTrainer().getUser().getUsername();


        Authentication authentication = new UsernamePasswordAuthenticationToken(traineeUsername, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Arrange
        when(traineeServiceImpl.findByValidatedUsername(anyString())).thenReturn(sampleTraining.getTrainee());
        when(trainerServiceImpl.findByValidatedUsername(anyString())).thenReturn(sampleTraining.getTrainer());
        when(traineeServiceImpl.hasTrainer(sampleTraining.getTrainee().getUser().getUsername(), sampleTraining.getTrainer().getUser().getUsername())).thenReturn(false);
        when(trainingRepository.save(any())).thenReturn(sampleTraining);
        // Act
        trainingServiceImpl.createTraining(trainingName,trainingDate,trainingDuration,traineeUsername,trainerUsername);

        // Assert
        verify(trainingRepository, times(1)).save(any());
    }

    @Test
    void update_ValidData_ReturnsUpdatedTraining() {
        // Arrange
        Long trainingId = 1L;
        Training updatedTraining = new Training();
        updatedTraining.setId(trainingId);

        User trainerUser = new User();
        User traineeUser = new User();

        trainerUser.setUsername("trainerUser");
        traineeUser.setUsername("traineeUser");

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(2L);
        updatedTrainer.setUser(trainerUser);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(3L);
        updatedTrainee.setUser(traineeUser);

        TrainingType updatedTrainingType = new TrainingType();
        updatedTrainingType.setId(4L);

        updatedTraining.setTrainer(updatedTrainer);
        updatedTraining.setTrainee(updatedTrainee);
        updatedTraining.setTrainingDate(LocalDate.now());
        updatedTraining.setTrainingDuration(BigDecimal.TEN);
        updatedTraining.setTrainingType(updatedTrainingType);

        when(trainingRepository.save(any(Training.class))).thenReturn(updatedTraining);
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(sampleTraining));
        when(trainingRepository.existsAnotherTrainingByTraineeAndTrainer(any(), any(), any())).thenReturn(false);

        // Act
        Training result = trainingServiceImpl.update(trainingId, updatedTraining);

        // Assert
        assertEquals(updatedTraining, result);
        assertEquals(updatedTrainer, result.getTrainer());
        assertEquals(updatedTrainee, result.getTrainee());
        assertEquals(updatedTrainingType, result.getTrainingType());
    }
}