package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.entities.enums.ActionType;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.mappers.TrainerWorkloadMapper;
import com.epamlearning.reportmicroservice.repositories.TrainerWorkloadRepository;
import com.epamlearning.reportmicroservice.services.impl.TrainerWorkloadServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadMapper trainerWorkloadMapper;

    @Mock
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @InjectMocks
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    @Test
    void getAllTrainerWorkload() {
        // Arrange
        List<TrainerWorkload> expectedList = new ArrayList<>();
        when(trainerWorkloadRepository.findAll()).thenReturn(expectedList);

        // Act
        List<TrainerWorkload> resultList = trainerWorkloadService.getAllTrainerWorkload();

        // Assert
        assertEquals(expectedList, resultList);
    }

    @Test
    void getAllByUsername() {
        // Arrange
        String username = "testUsername";
        List<TrainerWorkload> expectedList = new ArrayList<>();
        when(trainerWorkloadRepository.findByUsername(username)).thenReturn(expectedList);

        // Act
        List<TrainerWorkload> resultList = trainerWorkloadService.getAllByUsername(username);

        // Assert
        assertEquals(expectedList, resultList);
    }

    @Test
    void manageTrainerWorkload_AddAction() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setActionType(ActionType.ADD);
        when(trainerWorkloadMapper.TrainerWorkloadRequestDTOToTrainerWorkload(requestDTO)).thenReturn(new TrainerWorkload());

        // Act
        assertDoesNotThrow(() -> trainerWorkloadService.manageTrainerWorkload(requestDTO));

        // Assert
        verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));
    }

    @Test
    void manageTrainerWorkload_DeleteAction() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        TrainerWorkload trainerWorkload = new TrainerWorkload();
        trainerWorkload.setTraineeUsername("testUsername");
        requestDTO.setActionType(ActionType.DELETE);
        when(trainerWorkloadMapper.TrainerWorkloadRequestDTOToTrainerWorkload(requestDTO)).thenReturn(trainerWorkload);
        doNothing().when(trainerWorkloadRepository).deleteAllByTraineeUsername(anyString());

        // Act
        assertDoesNotThrow(() -> trainerWorkloadService.manageTrainerWorkload(requestDTO));

        // Assert
        verify(trainerWorkloadRepository, times(1)).deleteAllByTraineeUsername(anyString());
    }


    @Test
    void manageTrainerWorkload_UnsupportedAction() {
        // Arrange
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        requestDTO.setActionType(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> trainerWorkloadService.manageTrainerWorkload(requestDTO));
    }
}
