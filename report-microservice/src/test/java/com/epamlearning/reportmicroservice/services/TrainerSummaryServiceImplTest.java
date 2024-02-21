package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.services.impl.TrainerSummaryServiceImpl;
import com.epamlearning.reportmicroservice.services.impl.TrainerWorkloadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TrainerSummaryServiceImplTest {

    @Mock
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    @InjectMocks
    private TrainerSummaryServiceImpl trainerSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculate_ValidUsername_ReturnsTrainerSummary() {
        // Arrange
        String username = "testUser";
        LocalDate now = LocalDate.now();
        TrainerWorkload workload1 = new TrainerWorkload();
        workload1.setFirstName("John");
        workload1.setLastName("Doe");
        workload1.setUsername(username);
        workload1.setActive(true);
        workload1.setTrainingDate(now.minusMonths(1));
        workload1.setTrainingDuration(BigDecimal.valueOf(1.5));

        TrainerWorkload workload2 = new TrainerWorkload();
        workload2.setFirstName("John");
        workload2.setLastName("Doe");
        workload2.setUsername(username);
        workload2.setActive(true);
        workload2.setTrainingDate(now.minusMonths(1));
        workload2.setTrainingDuration(BigDecimal.valueOf(2.5));

        List<TrainerWorkload> workloads = List.of(workload1, workload2);
        when(trainerWorkloadService.getAllByUsername(username)).thenReturn(workloads);

        // Act
        TrainerSummary summary = trainerSummaryService.calculate(username);

        // Assert
        assertNotNull(summary);
        assertEquals("John", summary.getFirstName());
        assertEquals("Doe", summary.getLastName());
        assertTrue(summary.isStatus());
        assertEquals(username, summary.getUsername());

        Map<String, Map<String, BigDecimal>> monthlySummary = summary.getMonthlySummary();
        assertNotNull(monthlySummary);
        assertTrue(monthlySummary.containsKey(String.valueOf(now.getYear())));
        assertTrue(monthlySummary.get(String.valueOf(now.getYear())).containsKey(now.minusMonths(1).getMonth().toString()));

        BigDecimal totalDuration = monthlySummary.get(String.valueOf(now.getYear())).get(now.minusMonths(1).getMonth().toString());
        assertEquals(BigDecimal.valueOf(4.0), totalDuration);
    }

    @Test
    void calculate_NoWorkloadFound_ThrowsNotFoundException() {
        // Arrange
        String username = "nonExistentUser";
        when(trainerWorkloadService.getAllByUsername(username)).thenReturn(List.of());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerSummaryService.calculate(username));
    }
}
