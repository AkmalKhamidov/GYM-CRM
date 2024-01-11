package com.epamlearning.services;

import com.epamlearning.daos.TrainingDAOImpl;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @Mock
    private TrainingDAOImpl trainingDAO;

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TraineeService  traineeService;
    @Mock
    private TrainerService  trainerService;

    private Training training;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        training = new Training();
//        training.setId(999L);
//        training.setTrainingName("TestTrainingName");
//        training.setTrainingDuration(new BigDecimal(150));
//        training.setTrainingDate(new Calendar.Builder().setDate(2023,12,10).build().getTime());
//    }
//
//    @Test
//    void saveTraining() {
//        Trainee trainee = new Trainee();
//        trainee.setId(999L);
//        training.setTrainee(trainee);
//        Trainer trainer = new Trainer();
//        trainer.setId(999L);
//        training.setTrainer(trainer);
//        when(traineeService.findById(anyLong())).thenReturn(Optional.of(trainee));
//        when(trainerService.findById(anyLong())).thenReturn(Optional.of(trainer));
//        when(trainingService.existsTraineeAndTrainerInTrainings(trainee, trainer)).thenReturn(true);
//        when(trainingDAO.saveOrUpdate(training)).thenReturn(Optional.of(training));
//
//        Optional<Training> savedTraining = trainingService.save(training);
//
//        assertTrue(savedTraining.isPresent());
//        assertEquals(training, savedTraining.get());
//    }
//
//    @Test
//    void findTrainingById() {
//        long trainingId = 999L;
//        Training training = new Training();
//        when(trainingDAO.findById(trainingId)).thenReturn(Optional.of(training));
//        Optional<Training> foundTraining = trainingService.findById(trainingId);
//
//        assertTrue(foundTraining.isPresent());
//        assertEquals(training, foundTraining.get());
//    }
//
//    @Test
//    void findAllTrainings() {
//        List<Optional<Training>> trainings = List.of(Optional.of(training));
//        when(trainingDAO.findAll()).thenReturn(trainings);
//
//        List<Optional<Training>> foundTrainings = trainingService.findAll();
//        assertEquals(1, foundTrainings.size());
//        assertEquals(trainings, foundTrainings);
//    }
//
//    @Test
//    void deleteTrainingById() {
//        long trainingId = 999L;
//        Trainee trainee = new Trainee();
//        trainee.setId(999L);
//        training.setTrainee(trainee);
//        Trainer trainer = new Trainer();
//        trainer.setId(999L);
//        training.setTrainer(trainer);
//        when(traineeService.findById(anyLong())).thenReturn(Optional.of(trainee));
//        when(trainerService.findById(anyLong())).thenReturn(Optional.of(trainer));
//        when(trainingDAO.findById(trainingId)).thenReturn(Optional.of(training));
//        when(traineeService.updateTrainersForTrainee(anyLong(), anyList())).thenReturn(Optional.of(trainee));
//
//        assertDoesNotThrow(() -> trainingService.deleteById(trainingId));
//        verify(trainingDAO, times(1)).delete(training);
//    }
//
//    @Test
//    void deleteTrainingByIdNotFound() {
//        long trainingId = 999L;
//        when(trainingDAO.findById(trainingId)).thenReturn(Optional.empty());
//
//        NotFoundException exception = assertThrows(NotFoundException.class, () -> trainingService.deleteById(trainingId));
//        assertEquals("Training with ID " + trainingId + " not found.", exception.getMessage());
//        verify(trainingDAO, never()).delete(any(Training.class));
//    }
//
//    @Test
//    void findByTraineeAndCriteria() {
//        User user = new User();
//        user.setId(999L);
//        user.setUsername("TestUsername");
//        Trainee trainee = new Trainee();
//        trainee.setId(999L);
//        trainee.setUser(user);
//        Date dateFrom = new Date();
//        Date dateTo = new Date();
//        TrainingType trainingType = new TrainingType();
//        trainingType.setId(999L);
//        training.setTrainee(trainee);
//
//        List<Optional<Training>> trainings = List.of(Optional.of(training));
//        when(trainingDAO.findByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType)).thenReturn(trainings);
//        when(traineeService.findByUsername(anyString())).thenReturn(Optional.of(trainee));
//
//        List<Optional<Training>> foundTrainings = trainingService.findByTraineeAndCriteria(training.getTrainee().getUser().getUsername(), dateFrom, dateTo, trainingType);
//
//        assertEquals(trainings, foundTrainings);
//    }
//
//    @Test
//    void findByTrainerAndCriteria() {
//        User user = new User();
//        user.setId(999L);
//        user.setUsername("TestUsername");
//        Trainer trainer = new Trainer();
//        trainer.setId(999L);
//        trainer.setUser(user);
//        Date dateFrom = new Date();
//        Date dateTo = new Date();
//        TrainingType trainingType = new TrainingType();
//        trainingType.setId(999L);
//        training.setTrainer(trainer);
//
//        List<Optional<Training>> trainings = List.of(Optional.of(training));
//        when(trainingDAO.findByTrainerAndCriteria(trainer, dateFrom, dateTo, trainingType)).thenReturn(trainings);
//        when(trainerService.findByUsername(anyString())).thenReturn(Optional.of(trainer));
//
//        List<Optional<Training>> foundTrainings = trainingService.findByTrainerAndCriteria(training.getTrainer().getUser().getUsername(), dateFrom, dateTo, trainingType);
//
//        assertEquals(trainings, foundTrainings);
//    }

}
