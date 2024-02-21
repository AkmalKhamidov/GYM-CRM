package com.epamlearning.dtos.trainer.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainerListResponseDTO implements BaseDTO {
        private String username;
        private String firstName;
        private String lastName;
        private TrainingTypeName trainingTypeName;
}
