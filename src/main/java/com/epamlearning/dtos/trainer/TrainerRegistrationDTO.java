package com.epamlearning.dtos.trainer;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationDTO implements BaseDTO {
    private String firstName;
    private String lastName;
    private Long specializationId;
}
