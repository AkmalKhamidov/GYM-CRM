package com.epamlearning.services;

import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;

import java.util.Date;
import java.util.List;

public interface TraineeService extends BaseService{
    // CREATE
    TraineeRegistrationResponseDTO createTrainee(String firstName, String lastName, String address, Date dateOfBirth);

    // UPDATE
    TraineeProfileResponseDTO update(String username, TraineeUpdateRequestDTO dto);
    TraineeProfileResponseDTO updateActive(String username, boolean active);
    List<TrainerListResponseDTO> updateTrainersForTrainee(String username, UpdateTrainersOfTraineeRequestDTO dto);

    // READ
    TraineeProfileResponseDTO findByUsername(String username);
    List<TraineeProfileResponseDTO> findAll();

    // DELETE
    void deleteByUsername(String username);
}
