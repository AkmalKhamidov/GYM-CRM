package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TrainingController;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.Training;
import com.epamlearning.entities.User;
import com.epamlearning.microservices.report.ActionType;
import com.epamlearning.microservices.report.ReportServiceClient;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import com.epamlearning.producer.MessageProducer;
import com.epamlearning.services.impl.TraineeServiceImpl;
import com.epamlearning.services.impl.TrainerServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import com.epamlearning.services.impl.TrainingTypeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingServiceImpl trainingService;
    @Mock
    private ReportServiceClient reportServiceClient;
    @Mock
    private TraineeServiceImpl traineeService;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private UserEngagementMetrics metrics;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController)
                .build();
    }

    @Test
    void addTraining_shouldReturnSuccess() throws Exception {
        // Mock request DTO
        TrainingAddRequestDTO requestDTO = new TrainingAddRequestDTO();
        requestDTO.setTrainingName("JavaXStack");
        requestDTO.setTrainingDate(LocalDate.now());
        requestDTO.setTrainingDuration(BigDecimal.valueOf(60));
        requestDTO.setTrainerUsername("John.Smith");
        requestDTO.setTraineeUsername("John.Doe");

        Training training = new Training();
        User trainerUser = new User();
        trainerUser.setUsername("John.Smith");
        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        User traineeUser = new User();
        Trainee trainee = new Trainee();
        traineeUser.setUsername("John.Doe");
        training.setTrainee(trainee);

        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(BigDecimal.valueOf(60));

        // Mock service method
        when(trainingService.createTraining(any(), any(), any(), any(), any())).thenReturn(training);

        // Configure ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // Perform POST request
        mockMvc.perform(post("/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Verify service method invocation
        verify(trainingService, times(1)).createTraining(any(), any(), any(), any(), any());
    }
}
