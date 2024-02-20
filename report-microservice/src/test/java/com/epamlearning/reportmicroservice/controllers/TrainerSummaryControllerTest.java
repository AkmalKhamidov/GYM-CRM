package com.epamlearning.reportmicroservice.controllers;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.services.impl.TrainerSummaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerSummaryControllerTest {

    @Mock
    private TrainerSummaryServiceImpl trainerSummaryService;

    @InjectMocks
    private TrainerSummaryController trainerSummaryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerSummaryController).build();
    }

    @Test
    void getByUsername_ValidUsername_ReturnsTrainerSummary() throws Exception {
        // Arrange
        String username = "testUser";
        TrainerSummary trainerSummary = createMockTrainerSummary();
        when(trainerSummaryService.calculate(username)).thenReturn(trainerSummary);

        // Act & Assert
        mockMvc.perform(get("/monthly-summary")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(trainerSummaryService, times(1)).calculate(username);
    }

    @Test
    void getByUsername_NullUsername_ThrowsException() throws Exception {
        // Arrange
        String username = null;

        // Act & Assert
        mockMvc.perform(get("/monthly-summary")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).calculate(anyString());
    }

    @Test
    void getByUsername_BlankUsername_ThrowsException() throws Exception {
        // Arrange
        String username = "";

        // Act & Assert
        mockMvc.perform(get("/monthly-summary")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).calculate(anyString());
    }

    private TrainerSummary createMockTrainerSummary() {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setUsername("testUser");
        trainerSummary.setFirstName("John");
        trainerSummary.setLastName("Doe");
        trainerSummary.setStatus(true);
        trainerSummary.setYears(List.of(2022, 2023, 2024));

        Map<String, Map<String, BigDecimal>> monthlySummaryMap = new HashMap<>();
        Map<String, BigDecimal> monthData = new HashMap<>();
        monthData.put("duration", BigDecimal.valueOf(20.5));
        monthlySummaryMap.put("2023", monthData);

        trainerSummary.setMonthlySummary(monthlySummaryMap);
        return trainerSummary;
    }
}
