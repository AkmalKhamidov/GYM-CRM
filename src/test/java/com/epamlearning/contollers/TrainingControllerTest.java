package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TrainingController;
import com.epamlearning.dtos.training.request.TraineeTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainerTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.*;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.epamlearning.mapper.TrainingMapper;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.TrainingTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TrainingControllerTest {

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private UserEngagementMetrics metrics;
    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void getTraineeTrainings_shouldReturnTraineeTrainings() throws Exception {
        // Arrange
        TraineeTrainingsRequestDTO requestDTO = new TraineeTrainingsRequestDTO();
        requestDTO.setUsername("John.Doe");

        List<Training> trainings = new ArrayList<>(); // Set up your trainings object here
        List<TraineeTrainingsResponseDTO> expectedResponseDTO = new ArrayList<>(); // Set up your expected response object here

        when(trainingService.findByTraineeAndCriteria(any(), any(), any(), any(), any())).thenReturn(trainings);
        when(trainingMapper.trainingsToTraineeTrainingsResponseDTOs(any())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/training/by-trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", requestDTO.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

        // Verify
        verify(trainingService, times(1)).findByTraineeAndCriteria(any(), any(), any(), any(), any());
    }

    @Test
    void getTrainerTrainings_shouldReturnTrainerTrainings() throws Exception {
        // Arrange
        TrainerTrainingsRequestDTO requestDTO = new TrainerTrainingsRequestDTO();
        requestDTO.setUsername("John.Doe");

        List<Training> trainings = new ArrayList<>(); // Set up your trainings object here
        List<TrainerTrainingsResponseDTO> expectedResponseDTO = new ArrayList<>(); // Set up your expected response object here

        when(trainingService.findByTrainerAndCriteria(any(), any(), any(), any(), any())).thenReturn(trainings);
        when(trainingMapper.trainingsToTrainerTrainingsResponseDTOs(any())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/training/by-trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", requestDTO.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

        // Verify
        verify(trainingService, times(1)).findByTrainerAndCriteria(any(), any(), any(), any(), any());
    }

    @Test
    void addTraining_shouldReturnCreated() throws Exception {
        // Arrange
        TrainingAddRequestDTO requestDTO = new TrainingAddRequestDTO();
        requestDTO.setTraineeUsername("TraineeUser");
        requestDTO.setTrainerUsername("TrainerUser");
        requestDTO.setTrainingName("Java Basics");
        requestDTO.setTrainingDate(LocalDate.of(2024, Month.FEBRUARY, 28));
        requestDTO.setTrainingDuration(BigDecimal.valueOf(10));

        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(requestDTO.getTraineeUsername());
        trainee.setUser(user);

        Trainer trainer = new Trainer();
        User user1 = new User();
        user.setUsername(requestDTO.getTrainerUsername());
        trainer.setUser(user1);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName(TrainingTypeName.GYM_TYPE);
        trainer.setSpecialization(trainingType);

        Training training = new Training(); // Set up your training object here

        when(traineeService.findByUsername(requestDTO.getTraineeUsername())).thenReturn(trainee);
        when(trainerService.findByUsername(requestDTO.getTrainerUsername())).thenReturn(trainer);
        when(trainingService.createTraining(any(), any(), any(), any(), any(), any())).thenReturn(training);
        when(trainingService.save(any())).thenReturn(training);

        // Configure ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/training/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Training added successfully.")));

        // Verify
        verify(traineeService, times(1)).findByUsername(any());
        verify(trainerService, times(1)).findByUsername(any());
        verify(trainingService, times(1)).createTraining(any(), any(), any(), any(), any(), any());
        verify(trainingService, times(1)).save(any());
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
