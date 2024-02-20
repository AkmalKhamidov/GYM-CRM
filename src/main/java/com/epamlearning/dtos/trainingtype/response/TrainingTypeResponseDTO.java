package com.epamlearning.dtos.trainingtype.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainingTypeResponseDTO implements BaseDTO {
    private Long trainingTypeId;
    private TrainingTypeName trainingTypeName;
}
