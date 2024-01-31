package com.epamlearning.services;

import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.TrainingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TrainingService extends BaseService{

    // CREATE
    void createTraining(String trainingName,
                        LocalDate trainingDate,
                        BigDecimal trainingDuration,
                        String trainerUsername,
                        String traineeUsername);

    // READ
    List<TraineeTrainingsResponseDTO> findByTraineeAndCriteria(String traineeUsername, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String trainerName);

    List<TrainerTrainingsResponseDTO> findByTrainerAndCriteria(String trainerUsername, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String traineeName);

}
