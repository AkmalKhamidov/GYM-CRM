package com.epamlearning.contollers;

import com.epamlearning.controllers.TrainingTypeController;
import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.mapper.TrainingTypeMapper;
import com.epamlearning.services.TrainingTypeService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController).build();
    }

    @Test
    void getAllTrainingTypes_shouldReturnTrainingTypes() throws Exception {
        // Arrange
        List<TrainingTypeResponseDTO> expectedResponseDTO = Collections.singletonList(new TrainingTypeResponseDTO());

        when(trainingTypeService.findAll()).thenReturn(Collections.emptyList()); // Set up your training types object here
        when(trainingTypeMapper.trainingTypesToTrainingTypeResponseDTOs(anyList())).thenReturn(expectedResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/training-type/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedResponseDTO.size()));

        // Verify
        verify(trainingTypeService, times(1)).findAll();
        verify(trainingTypeMapper, times(1)).trainingTypesToTrainingTypeResponseDTOs(anyList());
    }
}
