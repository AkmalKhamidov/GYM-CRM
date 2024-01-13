package com.epamlearning.dtos.trainee.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.models.Trainer;

import java.util.Date;
import java.util.List;

public record TraineeProfileDTO(
        String firstName,
        String lastName,
        Date dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerListResponseDTO> trainerList) implements BaseDTO {
}
