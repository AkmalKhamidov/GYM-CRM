package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TraineeController;
import com.epamlearning.dtos.trainee.request.TraineeRegistrationRequestDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.microservices.report.ActionType;
import com.epamlearning.microservices.report.ReportServiceClient;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import com.epamlearning.services.impl.TraineeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@TestPropertySource(properties = {"server.servlet.context-path="})
public class TraineeControllerTest {

  @Mock
  private TraineeServiceImpl traineeService;

  @Mock
  private ReportServiceClient reportServiceClient;
  @Mock
  private UserEngagementMetrics metrics;

  @InjectMocks
  private TraineeController traineeController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(traineeController)
        .build();
  }

  @Test
  void registerTrainee_shouldReturnCreated() throws Exception {
    TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
    requestDTO.setFirstName("John");
    requestDTO.setLastName("Doe");
    requestDTO.setAddress("123 Main St");
    requestDTO.setDateOfBirth(new Date());

    TraineeRegistrationResponseDTO responseDTO = new TraineeRegistrationResponseDTO();

    when(traineeService.createTrainee(any(), any(), any(), any())).thenReturn(responseDTO);

    mockMvc.perform(post("/trainee/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestDTO)))
        .andExpect(status().isCreated());

    verify(traineeService, times(1)).createTrainee(any(), any(), any(), any());
  }

  @Test
  void getTraineeProfile_shouldReturnTraineeProfile() throws Exception {
    TraineeProfileResponseDTO expectedResponseDTO = new TraineeProfileResponseDTO();
    expectedResponseDTO.setFirstName("John");
    expectedResponseDTO.setLastName("Doe");
    expectedResponseDTO.setActive(true);
    expectedResponseDTO.setDateOfBirth(new Date());
    expectedResponseDTO.setAddress("123 Main St");
    expectedResponseDTO.setTrainerList(List.of());

    when(traineeService.findByUsername(anyString())).thenReturn(new TraineeProfileResponseDTO());

    mockMvc.perform(get("/trainee/John.Doe")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(traineeService, times(1)).findByUsername(anyString());
  }

  @Test
  void updateTraineeProfile_shouldReturnUpdatedTraineeProfile() throws Exception {
    TraineeUpdateRequestDTO updateRequestDTO = new TraineeUpdateRequestDTO();
    updateRequestDTO.setFirstName("John");
    updateRequestDTO.setLastName("Doe");
    updateRequestDTO.setActive(true);

    TraineeProfileResponseDTO updatedResponseDTO = new TraineeProfileResponseDTO();
    updatedResponseDTO.setFirstName(updateRequestDTO.getFirstName());
    updatedResponseDTO.setLastName(updateRequestDTO.getLastName());
    updatedResponseDTO.setActive(updateRequestDTO.isActive());

    when(traineeService.update(anyString(), any())).thenReturn(updatedResponseDTO);

    mockMvc.perform(put("/trainee/John.Doe")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(updateRequestDTO)))
        .andExpect(status().isOk());

    verify(traineeService, times(1)).update(anyString(), any());
  }

  @Test
  void deleteTrainee_shouldReturnOk() throws Exception {
    TrainerWorkloadDTO trainerWorkloadDTO = new TrainerWorkloadDTO();
    trainerWorkloadDTO.setTraineeUsername("John.Doe");
    trainerWorkloadDTO.setActionType(ActionType.DELETE);
    doNothing().when(traineeService).deleteByUsername(anyString());
    mockMvc.perform(delete("/trainee/John.Doe")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(traineeService, times(1)).deleteByUsername(anyString());
  }

  @Test
  void updateTrainersOfTrainee_shouldReturnUpdatedTrainersList() throws Exception {
    UpdateTrainersOfTraineeRequestDTO trainersOfTraineeDTO = new UpdateTrainersOfTraineeRequestDTO();
    trainersOfTraineeDTO.setTrainers(List.of(new TrainerUsernameRequestDTO()));

    List<TrainerListResponseDTO> expectedResponseDTO = List.of(new TrainerListResponseDTO());

    when(traineeService.updateTrainersForTrainee(anyString(),
        (UpdateTrainersOfTraineeRequestDTO) any())).thenReturn(expectedResponseDTO);

    mockMvc.perform(put("/trainee/John.Doe/trainers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(trainersOfTraineeDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

    verify(traineeService, times(1)).updateTrainersForTrainee(anyString(),
        (UpdateTrainersOfTraineeRequestDTO) any());
  }

  @Test
  void updateTraineeActive_shouldReturnOk() throws Exception {
    String username = "John.Doe";
    boolean isActive = true;

    mockMvc.perform(patch("/trainee/" + username + "/" + isActive)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(traineeService, times(1)).updateActive(username, isActive);
  }

  // Helper method to convert objects to JSON string
  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
