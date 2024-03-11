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
        when(trainerSummaryService.findByUsername(username)).thenReturn(trainerSummary);

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/{username}", username)
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(trainerSummaryService, times(1)).findByUsername(username);
    }

    @Test
    void getByUsername_BlankUsername_ThrowsException() throws Exception {
        // Arrange
        String username = " ";

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/{username}", username)
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).findByUsername(anyString());
    }

    @Test
    void getByFirstNameAndLastName_Valid_ReturnsTrainerSummary() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";;
        List<TrainerSummary> trainerSummaries = List.of(createMockTrainerSummary());
        when(trainerSummaryService.findByFirstNameAndLastName(firstName, lastName)).thenReturn(trainerSummaries);

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(trainerSummaryService, times(1)).findByFirstNameAndLastName(firstName, lastName);
    }


    @Test
    void getByFirstNameAndLastName_NullFirstName_ThrowsException() throws Exception {
        // Arrange
        String firstName = null;
        String lastName = "Doe";;

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).findByUsername(anyString());
    }


    @Test
    void getByFirstNameAndLastName_NullLastName_ThrowsException() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = null;

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).findByUsername(anyString());
    }

    @Test
    void getByFirstNameAndLastName_BlankFirstName_ThrowsException() throws Exception {
        // Arrange
        String firstName = "";
        String lastName = "Doe";

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).findByUsername(anyString());
    }


    @Test
    void getByFirstNameAndLastName_BlankLastName_ThrowsException() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "";

        // Act & Assert
        mockMvc.perform(get("/monthly-summary/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(trainerSummaryService, never()).findByUsername(anyString());
    }

    private TrainerSummary createMockTrainerSummary() {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setUsername("testUser");
        trainerSummary.setFirstName("John");
        trainerSummary.setLastName("Doe");
        trainerSummary.setStatus(true);

        Map<Integer, Map<String, BigDecimal>> monthlySummaryMap = new HashMap<>();
        Map<String, BigDecimal> monthData = new HashMap<>();
        monthData.put("duration", BigDecimal.valueOf(20.5));
        monthlySummaryMap.put(2023, monthData);

        trainerSummary.setMonthlySummary(monthlySummaryMap);
        return trainerSummary;
    }
}
