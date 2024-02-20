package com.epamlearning.dtos.trainer.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainee.response.TraineeListResponseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainerProfileResponseDTO implements BaseDTO {
    private String firstName;
    private String lastName;
    private TrainingTypeName trainingTypeName;
    private boolean isActive;
    private List<TraineeListResponseDTO> trainees;
}
