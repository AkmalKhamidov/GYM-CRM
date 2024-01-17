package com.epamlearning.dtos.trainer.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainee.response.TraineeListResponseDTO;
import com.epamlearning.models.enums.TrainingTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponseDTO implements BaseDTO {
    private String firstName;
    private String lastName;
    private TrainingTypeName trainingTypeName;
    private boolean isActive;
    private List<TraineeListResponseDTO> traineeList;
}
