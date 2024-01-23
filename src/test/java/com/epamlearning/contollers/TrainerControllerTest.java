package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TrainerController;
import com.epamlearning.dtos.trainer.request.TrainerRegistrationRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.User;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.services.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TrainerControllerTest {

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private UserEngagementMetrics metrics;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    void registerTrainer_shouldReturnCreated() throws Exception {
        // Arrange
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setSpecializationId(1L);

        Trainer trainer = new Trainer(); // Set up your trainer object here

        when(trainerService.createTrainer(any(), any(), any())).thenReturn(trainer);
        when(trainerService.save(any())).thenReturn(trainer);
        when(trainerMapper.trainerToUserAuthDTO(any())).thenReturn(new UserAuthDTO("John.Doe", "password"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("password"));

        // Verify
        verify(trainerService, times(1)).save(any());
    }

    @Test
    void getTrainerProfile_shouldReturnTrainerProfile() throws Exception {
        // Arrange
        TrainerProfileResponseDTO expectedResponseDTO = new TrainerProfileResponseDTO();
        expectedResponseDTO.setFirstName("John");
        expectedResponseDTO.setLastName("Doe");
        expectedResponseDTO.setActive(true);

        when(trainerService.findByUsername(anyString())).thenReturn(new Trainer()); // Set up your trainer object here
        when(trainerMapper.trainerToTrainerProfileResponseDTO(any())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/trainer/John.Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedResponseDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponseDTO.getLastName()))
                .andExpect(jsonPath("$.active").value(expectedResponseDTO.isActive()));

        // Verify
        verify(trainerService, times(1)).findByUsername(anyString());
    }

    @Test
    void updateTraineeProfile_shouldReturnUpdatedTrainerProfile() throws Exception {
        // Arrange
        TrainerUpdateRequestDTO updateRequestDTO = new TrainerUpdateRequestDTO();
        updateRequestDTO.setUsername("John.Doe");
        updateRequestDTO.setFirstName("John");
        updateRequestDTO.setLastName("Doe");

        Trainer updatedTrainer = new Trainer();
        User user = new User();
        user.setFirstName(updateRequestDTO.getFirstName());
        user.setLastName(updateRequestDTO.getLastName());
        updatedTrainer.setUser(user);
        updatedTrainer.setId(1L);

        when(trainerService.findByUsername(updateRequestDTO.getUsername())).thenReturn(updatedTrainer); // Set up your trainer object here
        when(trainerService.update(anyLong(), any())).thenReturn(updatedTrainer);
        when(trainerMapper.trainerToTrainerProfileResponseDTO(any()))
                .thenReturn(new TrainerProfileResponseDTO(updateRequestDTO.getFirstName(), updateRequestDTO.getLastName(), null,
        true, null));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/trainer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateRequestDTO.getLastName()))
                .andExpect(jsonPath("$.active").value(true));

        // Verify
        verify(trainerService, times(1)).findByUsername(anyString());
        verify(trainerService, times(1)).update(anyLong(), any());
    }

    @Test
    void getNotAssignedTrainers_shouldReturnNotAssignedTrainers() throws Exception {
        // Arrange
        String traineeUsername = "John.Doe";
        List<TrainerListResponseDTO> expectedResponseDTO = Collections.singletonList(new TrainerListResponseDTO());

        when(trainerService.findNotAssignedActiveTrainers(anyString())).thenReturn(Collections.emptyList()); // Set up your trainers object here
        when(trainerMapper.trainersToTrainerListResponseDTOs(anyList())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/trainer/not-assigned-on-trainee/" + traineeUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

        // Verify
        verify(trainerService, times(1)).findNotAssignedActiveTrainers(anyString());
    }

    @Test
    void updateTraineeActive_shouldReturnOk() throws Exception {
        // Arrange
        boolean isActive = true;
        String username = "John.Doe";
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername(username);
        user.setActive(true);
        trainer.setUser(user);
        trainer.setId(1L);
        when(trainerService.findByUsername(username)).thenReturn(trainer); // Set up your trainer object here

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/trainer/updateActive/" + username + "/" + isActive)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)))
                .andExpect(content().string(containsString("Trainer with username: " + username +" was activated.")));

        // Verify
        verify(trainerService, times(1)).updateActive(anyLong(), anyBoolean());
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