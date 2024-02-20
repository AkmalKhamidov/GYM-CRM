package com.epamlearning.dtos.training.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrainerTrainingsResponseDTO implements BaseDTO {

    private String trainingName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date trainingDate;

    private TrainingTypeName trainingTypeName;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal trainingDuration;

    private String traineeFullName;

}
