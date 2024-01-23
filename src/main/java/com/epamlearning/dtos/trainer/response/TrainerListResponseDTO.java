package com.epamlearning.dtos.trainer.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerListResponseDTO implements BaseDTO {
        private String username;
        private String firstName;
        private String lastName;
        private TrainingTypeName trainingTypeName;
}
