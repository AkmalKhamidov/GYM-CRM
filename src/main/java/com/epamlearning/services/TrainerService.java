package com.epamlearning.services;

import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import java.util.List;

public interface TrainerService extends BaseService{
    // CREATE
    TrainerRegistrationResponseDTO createTrainer(String firstName, String lastName, Long trainingTypeId);

    // UPDATE
    TrainerProfileResponseDTO update(String username, TrainerUpdateRequestDTO dto);
    TrainerProfileResponseDTO updateActive(String username, boolean active);
    List<TrainerListResponseDTO> findNotAssignedActiveTrainers(String username);

    // READ
    TrainerProfileResponseDTO findByUsername(String username);
    List<TrainerProfileResponseDTO> findAll();
}
