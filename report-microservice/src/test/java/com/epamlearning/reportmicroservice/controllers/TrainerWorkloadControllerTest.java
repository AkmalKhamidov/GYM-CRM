package com.epamlearning.reportmicroservice.controllers;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.enums.ActionType;
import com.epamlearning.reportmicroservice.services.impl.TrainerWorkloadServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerWorkloadControllerTest {

    @Mock
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    @InjectMocks
    private TrainerWorkloadController controller;

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void manageTrainerWorkload_AddAction_Success() throws Exception {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setTraineeUsername("testUser");
        requestDTO.setActionType(ActionType.ADD);
        doNothing().when(trainerWorkloadService).manageTrainerWorkload(requestDTO);

        // Act & Assert
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isOk());
        verify(trainerWorkloadService, times(1)).manageTrainerWorkload(any(TrainerWorkloadRequestDTO.class));
    }

    @Test
    void manageTrainerWorkload_DeleteAction_Success() throws Exception {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setTraineeUsername("testUser");
        requestDTO.setActionType(ActionType.DELETE);
        doNothing().when(trainerWorkloadService).manageTrainerWorkload(requestDTO);

        // Act & Assert
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isOk());
        verify(trainerWorkloadService, times(1)).manageTrainerWorkload(any(TrainerWorkloadRequestDTO.class));
    }

    @Test
    void manageTrainerWorkload_MissingActionType_ReturnsBadRequest() throws Exception {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setTraineeUsername("testUser");

        // Act & Assert
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isBadRequest());

        // Assert
        verify(trainerWorkloadService, never()).manageTrainerWorkload(requestDTO);
    }

    @Test
    void manageTrainerWorkload_MissingTraineeUsername_ReturnsBadRequest() throws Exception {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setActionType(ActionType.DELETE);

        // Act & Assert
        mockMvc.perform(post("/trainer-workload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isBadRequest());

        // Assert
        verify(trainerWorkloadService, never()).manageTrainerWorkload(requestDTO);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
