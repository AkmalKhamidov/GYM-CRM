package com.epamlearning.services;

import com.epamlearning.entities.*;
import com.epamlearning.repositories.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingService trainingService;

    private Training sampleTraining;

    @BeforeEach
    void setUp() {
        sampleTraining = new Training();
        sampleTraining.setId(1L);

        Trainer sampleTrainer = new Trainer();
        sampleTrainer.setId(1L);
        sampleTraining.setTrainer(sampleTrainer);

        Trainee sampleTrainee = new Trainee();
        sampleTrainee.setId(2L);
        sampleTraining.setTrainee(sampleTrainee);

        TrainingType sampleTrainingType = new TrainingType();
        sampleTrainingType.setId(3L);
        sampleTraining.setTrainingType(sampleTrainingType);

        sampleTraining.setTrainingName("Sample Training");
        sampleTraining.setTrainingDate(LocalDate.now());
        sampleTraining.setTrainingDuration(BigDecimal.TEN);
    }

    @Test
    void save_ValidTraining_ReturnsSavedTraining() {
        // Arrange
        when(trainingRepository.save(sampleTraining)).thenReturn(sampleTraining);
        when(traineeService.hasTrainer(sampleTraining.getTrainee().getId(), sampleTraining.getTrainer().getId())).thenReturn(false);
        when(trainingRepository.existsAnotherTrainingByTraineeAndTrainer(any(), any(), any())).thenReturn(false);

        // Act
        Training result = trainingService.save(sampleTraining);

        // Assert
        assertEquals(sampleTraining, result);
    }

    @Test
    void save_TrainingWithExistingAnotherTrainingForTraineeAndTrainer_ReturnsSavedTrainingWithUpdatedTrainerList() {
        // Arrange
        when(trainingRepository.save(sampleTraining)).thenReturn(sampleTraining);
        when(traineeService.hasTrainer(sampleTraining.getTrainee().getId(), sampleTraining.getTrainer().getId())).thenReturn(false);
        when(trainingRepository.existsAnotherTrainingByTraineeAndTrainer(any(), any(), any())).thenReturn(true);
        when(trainingRepository.findById(sampleTraining.getId())).thenReturn(Optional.of(sampleTraining));

        // Act
        Training result = trainingService.save(sampleTraining);

        // Assert
        assertEquals(sampleTraining, result);
        verify(traineeService, times(1)).updateTrainersForTrainee(any(), any());
    }

    @Test
    void findById_ValidId_ReturnsTraining() {
        // Arrange
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(sampleTraining));

        // Act
        Training result = trainingService.findById(1L);

        // Assert
        assertEquals(sampleTraining, result);
    }

    @Test
    void findById_NullId_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainingService.findById(null));
    }

    // Similar tests for findById_NonExistentId, findAll, findByTraineeAndCriteria, findByTrainerAndCriteria, etc.

    @Test
    void update_ValidData_ReturnsUpdatedTraining() {
        // Arrange
        Long trainingId = 1L;
        Training updatedTraining = new Training();
        updatedTraining.setId(trainingId);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(2L);

        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(3L);

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
        Training result = trainingService.update(trainingId, updatedTraining);

        // Assert
        assertEquals(updatedTraining, result);
        assertEquals(updatedTrainer, result.getTrainer());
        assertEquals(updatedTrainee, result.getTrainee());
        assertEquals(updatedTrainingType, result.getTrainingType());
    }

    @Test
    void update_NullTraining_ThrowsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> trainingService.update(1L, null));
    }

    @Test
    void deleteById_ValidId_DeletesTrainingAndUpdatesTrainerListForTrainee() {
        // Arrange
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(sampleTraining));
        when(trainingRepository.existsAnotherTrainingByTraineeAndTrainer(any(), any(), any())).thenReturn(false);

        // Act
        trainingService.deleteById(trainingId);

        // Assert
        verify(trainingRepository, times(1)).delete(sampleTraining);
        verify(traineeService, times(1)).updateTrainersForTrainee(any(), any());
    }

    @Test
    void deleteById_ValidId_TrainingWithAnotherTrainingExists_DoesNotUpdateTrainerListForTrainee() {
        // Arrange
        Long trainingId = 1L;
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(sampleTraining));
        when(trainingRepository.existsAnotherTrainingByTraineeAndTrainer(any(), any(), any())).thenReturn(true);

        // Act
        trainingService.deleteById(trainingId);

        // Assert
        verify(trainingRepository, times(1)).delete(sampleTraining);
        verify(traineeService, times(0)).updateTrainersForTrainee(any(), any());
    }

}