package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TrainingController;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingServiceImpl trainingService;


    @Mock
    private TraineeServiceImpl traineeService;

    @Mock
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private UserEngagementMetrics metrics;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
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

        // Mock service method
        doNothing().when(trainingService).createTraining(any(), any(), any(), any(), any());

        // Configure ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        // Perform POST request
        mockMvc.perform(post("/api/v1/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Training added successfully."));

        // Verify service method invocation
        verify(trainingService, times(1)).createTraining(any(), any(), any(), any(), any());
    }
}
