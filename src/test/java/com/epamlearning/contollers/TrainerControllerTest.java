package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TrainerController;
import com.epamlearning.dtos.trainer.request.TrainerRegistrationRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.dtos.training.request.TrainerTrainingsRequestDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.services.impl.TrainerServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerControllerTest {

  @Mock
  private TrainerServiceImpl trainerService;

  @Mock
  private TrainingServiceImpl trainingService;

  @Mock
  private UserEngagementMetrics metrics;

  @InjectMocks
  private TrainerController trainerController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(trainerController)
        .build();
  }

  @Test
  void registerTrainer_shouldReturnCreated() throws Exception {
    TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
    requestDTO.setFirstName("John");
    requestDTO.setLastName("Doe");
    requestDTO.setSpecializationId(1L);

    TrainerRegistrationResponseDTO responseDTO = new TrainerRegistrationResponseDTO();
    responseDTO.setUsername("John.Doe");

    when(trainerService.createTrainer(requestDTO.getFirstName(), requestDTO.getLastName(),
        requestDTO.getSpecializationId()))
        .thenReturn(responseDTO);

    mockMvc.perform(post("/trainer/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(responseDTO.getUsername()));

    verify(trainerService, times(1)).createTrainer(requestDTO.getFirstName(),
        requestDTO.getLastName(), requestDTO.getSpecializationId());
  }

  @Test
  void getTrainerProfile_shouldReturnTrainerProfile() throws Exception {
    String firstName = "John";
    String lastName = "Doe";
    String username = "John.Doe";
    TrainerProfileResponseDTO responseDTO = new TrainerProfileResponseDTO();
    responseDTO.setFirstName(firstName);
    responseDTO.setLastName(lastName);

    when(trainerService.findByUsername(username)).thenReturn(responseDTO);

    mockMvc.perform(get("/trainer/{username}", username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(responseDTO.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(responseDTO.getLastName()))
    ;

    verify(trainerService, times(1)).findByUsername(username);
  }

  @Test
  void updateTrainerProfile_shouldReturnUpdatedTrainerProfile() throws Exception {
    String username = "John";
    TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
    requestDTO.setFirstName("John");
    requestDTO.setLastName("Doe");
    requestDTO.setActive(true);

    TrainerProfileResponseDTO responseDTO = new TrainerProfileResponseDTO();
    responseDTO.setFirstName(requestDTO.getFirstName());
    responseDTO.setLastName(requestDTO.getLastName());
    responseDTO.setActive(requestDTO.isActive());

    when(trainerService.update(username, requestDTO)).thenReturn(responseDTO);

    mockMvc.perform(put("/trainer/{username}", username)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestDTO)))
        .andExpect(status().isOk());

    verify(trainerService, times(1)).update(anyString(), any());
  }

  @Test
  void updateTrainerActive_shouldReturnOk() throws Exception {
    String username = "John";
    boolean isActive = true;

    mockMvc.perform(patch("/trainer/{username}/{active}", username, isActive))
        .andExpect(status().isOk());

    verify(trainerService, times(1)).updateActive(username, isActive);
  }

  @Test
  void getTrainerTrainings_shouldReturnTrainerTrainings() throws Exception {
    String username = "John";
    TrainerTrainingsRequestDTO requestDTO = new TrainerTrainingsRequestDTO();
    requestDTO.setDateFrom(LocalDate.from(LocalDate.now()));
    requestDTO.setDateTo(LocalDate.of(2023, Month.FEBRUARY, 20));
    requestDTO.setTraineeName("Alice");

    List<TrainerTrainingsResponseDTO> responseDTOList = List.of(new TrainerTrainingsResponseDTO());

    when(trainingService.findByTrainerAndCriteria(username, requestDTO.getDateFrom(),
        requestDTO.getDateTo(), null, requestDTO.getTraineeName()))
        .thenReturn(responseDTOList);

    mockMvc.perform(get("/trainer/{username}/trainings", username)
            .param("dateFrom", String.valueOf(requestDTO.getDateFrom()))
            .param("dateTo", String.valueOf(requestDTO.getDateTo()))
            .param("traineeName", requestDTO.getTraineeName()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());

    verify(trainingService, times(1)).findByTrainerAndCriteria(username, requestDTO.getDateFrom(),
        requestDTO.getDateTo(), null, requestDTO.getTraineeName());
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
