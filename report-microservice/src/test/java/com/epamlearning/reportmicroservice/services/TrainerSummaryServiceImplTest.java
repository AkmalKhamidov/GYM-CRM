package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.enums.ActionType;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.repositories.TrainerSummaryRepository;
import com.epamlearning.reportmicroservice.services.impl.TrainerSummaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerSummaryServiceImplTest {

    @Mock
    private TrainerSummaryRepository trainerSummaryRepository;

    @InjectMocks
    private TrainerSummaryServiceImpl trainerSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByFirstNameAndLastName_ValidInput_ReturnsTrainerSummaryList() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        List<TrainerSummary> expectedList = Collections.singletonList(new TrainerSummary());
        when(trainerSummaryRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(expectedList);

        // Act
        List<TrainerSummary> resultList = trainerSummaryService.findByFirstNameAndLastName(firstName, lastName);

        // Assert
        assertEquals(expectedList, resultList);
    }

    @Test
    void findByFirstNameAndLastName_NullInput_ThrowsNotFoundException() {
        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerSummaryService.findByFirstNameAndLastName(null, null));
    }

    @Test
    void findByUsername_ValidInput_ReturnsTrainerSummary() {
        // Arrange
        String username = "testUsername";
        TrainerSummary expectedTrainerSummary = new TrainerSummary();
        when(trainerSummaryRepository.findByUsername(username)).thenReturn(Optional.of(expectedTrainerSummary));

        // Act
        TrainerSummary resultTrainerSummary = trainerSummaryService.findByUsername(username);

        // Assert
        assertEquals(expectedTrainerSummary, resultTrainerSummary);
    }

    @Test
    void findByUsername_NullInput_ThrowsNotFoundException() {
        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerSummaryService.findByUsername(null));
    }

    @Test
    void update_AddAction_ReturnsUpdatedTrainerSummary() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setUsername("testUsername");
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setActive(true);
        requestDTO.setTrainingDate(LocalDate.now());
        requestDTO.setTrainingDuration(BigDecimal.TEN);
        requestDTO.setActionType(ActionType.ADD);

        TrainerSummary existingTrainerSummary = new TrainerSummary();
        existingTrainerSummary.setUsername("testUsername");
        Map<Integer, Map<String, BigDecimal>> monthlySummary = new HashMap<>();
        monthlySummary.put(YearMonth.now().getYear(), new HashMap<>(Map.of(YearMonth.now().getMonth().toString(), BigDecimal.valueOf(20))));
        existingTrainerSummary.setMonthlySummary(monthlySummary);

        when(trainerSummaryRepository.findByUsername("testUsername")).thenReturn(Optional.of(existingTrainerSummary));
        when(trainerSummaryRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TrainerSummary updatedTrainerSummary = trainerSummaryService.update(requestDTO);

        // Assert
        assertNotNull(updatedTrainerSummary);
        assertEquals(BigDecimal.valueOf(30), updatedTrainerSummary.getMonthlySummary().get(YearMonth.now().getYear()).get(YearMonth.now().getMonth().toString()));
    }

    @Test
    void update_DeleteAction_ReturnsUpdatedTrainerSummary() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setUsername("testUsername");
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setActive(true);
        requestDTO.setTrainingDate(LocalDate.now());
        requestDTO.setTrainingDuration(BigDecimal.TEN);
        requestDTO.setActionType(ActionType.DELETE);

        TrainerSummary existingTrainerSummary = new TrainerSummary();
        existingTrainerSummary.setUsername("testUsername");
        Map<Integer, Map<String, BigDecimal>> monthlySummary = new HashMap<>();
        monthlySummary.put(YearMonth.now().getYear(), new HashMap<>(Map.of(YearMonth.now().getMonth().toString(), BigDecimal.valueOf(20))));
        existingTrainerSummary.setMonthlySummary(monthlySummary);

        when(trainerSummaryRepository.findByUsername("testUsername")).thenReturn(Optional.of(existingTrainerSummary));
        when(trainerSummaryRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TrainerSummary updatedTrainerSummary = trainerSummaryService.update(requestDTO);

        // Assert
        assertNotNull(updatedTrainerSummary);
        assertEquals(BigDecimal.TEN, updatedTrainerSummary.getMonthlySummary().get(YearMonth.now().getYear()).get(YearMonth.now().getMonth().toString()));
    }

    @Test
    void update_NoExistingTrainerSummary_CreatesNewTrainerSummary() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setUsername("testUsername");
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setActive(true);
        requestDTO.setTrainingDate(LocalDate.now());
        requestDTO.setTrainingDuration(BigDecimal.TEN);
        requestDTO.setActionType(ActionType.ADD);

        when(trainerSummaryRepository.findByUsername("testUsername")).thenReturn(Optional.empty());
        when(trainerSummaryRepository.save(any(TrainerSummary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TrainerSummary createdTrainerSummary = trainerSummaryService.update(requestDTO);

        // Assert
        assertNotNull(createdTrainerSummary);
        assertEquals(BigDecimal.TEN, createdTrainerSummary.getMonthlySummary().get(YearMonth.now().getYear()).get(YearMonth.now().getMonth().toString()));
    }
}
