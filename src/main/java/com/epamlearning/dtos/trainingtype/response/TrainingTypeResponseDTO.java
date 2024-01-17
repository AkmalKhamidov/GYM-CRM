package com.epamlearning.dtos.trainingtype.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.enums.TrainingTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypeResponseDTO implements BaseDTO {
    private Long trainingTypeId;
    private TrainingTypeName trainingTypeName;
}
