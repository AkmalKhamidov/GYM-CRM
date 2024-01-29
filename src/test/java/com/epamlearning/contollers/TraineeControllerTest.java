package com.epamlearning.contollers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.controllers.TraineeController;
import com.epamlearning.dtos.trainee.request.TraineeRegistrationRequestDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.User;
import com.epamlearning.mapper.TraineeMapper;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.services.TraineeService;
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

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TraineeControllerTest {

    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private UserEngagementMetrics metrics;
    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TraineeController traineeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

    @Test
    void registerTrainee_shouldReturnCreated() throws Exception {
        // Arrange
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setAddress("123 Main St");
        requestDTO.setDateOfBirth(new Date());

        Trainee trainee = new Trainee(); // Set up your trainee object here

        when(traineeService.createTrainee(any(), any(), any(), any())).thenReturn(trainee);
        when(traineeService.save(any())).thenReturn(trainee);
        when(traineeMapper.traineeToUserAuthDTO(any())).thenReturn(new UserAuthDTO("John.Doe", "password"));
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Doe"))
                .andExpect(jsonPath("$.password").value("password"));

        // Verify
        verify(traineeService, times(1)).save(any());
    }

    @Test
    void getTraineeProfile_shouldReturnTraineeProfile() throws Exception {
        // Arrange
        TraineeProfileResponseDTO expectedResponseDTO = new TraineeProfileResponseDTO();
        expectedResponseDTO.setFirstName("John");
        expectedResponseDTO.setLastName("Doe");
        expectedResponseDTO.setActive(true);
        expectedResponseDTO.setDateOfBirth(new Date());
        expectedResponseDTO.setAddress("123 Main St");
        expectedResponseDTO.setTrainerList(List.of());

        when(traineeService.findByUsername(anyString())).thenReturn(new Trainee()); // Set up your trainee object here
        when(traineeMapper.traineeToTraineeProfileResponseToDTO(any())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/John.Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedResponseDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expectedResponseDTO.getLastName()))
                .andExpect(jsonPath("$.active").value(expectedResponseDTO.isActive()))
                .andExpect(jsonPath("$.address").value(expectedResponseDTO.getAddress()))
                .andExpect(jsonPath("$.trainerList").isArray());

        // Verify
        verify(traineeService, times(1)).findByUsername(anyString());
    }

    @Test
    void updateTraineeProfile_shouldReturnUpdatedTraineeProfile() throws Exception {
        String usernameToUpdate = "John.Doe";

        // Arrange
        TraineeUpdateRequestDTO updateRequestDTO = new TraineeUpdateRequestDTO();
        updateRequestDTO.setFirstName("John");
        updateRequestDTO.setLastName("Doe");
        updateRequestDTO.setActive(true);

        Trainee updatedTrainee = new Trainee();
        User user = new User();
        user.setFirstName(updateRequestDTO.getFirstName());
        user.setLastName(updateRequestDTO.getLastName());
        user.setActive(updateRequestDTO.isActive());
        updatedTrainee.setUser(user);
        updatedTrainee.setId(1L);
        when(traineeService.findByUsername(usernameToUpdate)).thenReturn(updatedTrainee); // Set up your trainee object here
        when(traineeService.update(1L, updatedTrainee)).thenReturn(updatedTrainee);
        when(traineeMapper.traineeToTraineeProfileResponseToDTO(any()))
                .thenReturn(new TraineeProfileResponseDTO(updateRequestDTO.getFirstName(), updateRequestDTO.getLastName(), updateRequestDTO.getDateOfBirth(), updateRequestDTO.getAddress(), updateRequestDTO.isActive(), null));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/trainee/" + usernameToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateRequestDTO.getLastName()))
                .andExpect(jsonPath("$.active").value(updateRequestDTO.isActive()))
                .andExpect(jsonPath("$.address").value(updateRequestDTO.getAddress()));

        // Verify
        verify(traineeService, times(1)).findByUsername(anyString());
        verify(traineeService, times(1)).update(anyLong(), any());
    }

    @Test
    void deleteTrainee_shouldReturnOk() throws Exception {
        String usernameToDelete = "John.Doe";
        // Arrange
        doNothing().when(traineeService).deleteByUsername(anyString());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/trainee/"+usernameToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("John.Doe")));

        // Verify
        verify(traineeService, times(1)).deleteByUsername(anyString());
    }

    @Test
    void updateTrainersOfTrainee_shouldReturnUpdatedTrainersList() throws Exception {
        // Arrange
        UpdateTrainersOfTraineeRequestDTO trainersOfTraineeDTO = new UpdateTrainersOfTraineeRequestDTO();
        trainersOfTraineeDTO.setTrainers(List.of(new TrainerUsernameRequestDTO("John.Smith"))); // Set up your trainer objects here
        String usernameParameter = "John.Doe";
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        User user = new User();
        user.setUsername("John.Doe");
        trainee.setUser(user);

        List<TrainerListResponseDTO> expectedResponseDTO = List.of(new TrainerListResponseDTO());

        Trainer trainer = new Trainer();
        trainer.setId(2L);
        User user1 =  new User();
        user1.setUsername("John.Smith");
        trainer.setUser(user1);

        when(traineeService.findByUsername(usernameParameter)).thenReturn(trainee); // Set up your trainee object here
        when(trainerService.findByUsername(trainersOfTraineeDTO.getTrainers().get(0).getUsername())).thenReturn(trainer); // Set up your trainer object here
        when(traineeService.updateTrainersForTrainee(anyLong(), anyList())).thenReturn(trainee); // Set up your updated trainee object here
        when(trainerMapper.trainersToTrainerListResponseDTOs(anyList())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/trainee/" + usernameParameter + "/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(trainersOfTraineeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

        // Verify
        verify(traineeService, times(1)).findByUsername(anyString());
        verify(trainerService, times(1)).findByUsername(anyString());
        verify(traineeService, times(1)).updateTrainersForTrainee(anyLong(), anyList());
    }

    @Test
    void updateTraineeActive_shouldReturnOk() throws Exception {
        // Arrange
        boolean isActive = true;
        String username = "John.Doe";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        user.setActive(true);
        trainee.setId(1L);
        when(traineeService.findByUsername(username)).thenReturn(trainee); // Set up your trainee object here

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/trainee/" + username + "/" + isActive)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)))
                .andExpect(content().string(containsString("Trainee with username: " + username +" was activated.")));

        // Verify
        verify(traineeService, times(1)).updateActive(anyLong(), anyBoolean());
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
