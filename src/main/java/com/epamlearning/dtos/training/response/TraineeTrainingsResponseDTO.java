package com.epamlearning.dtos.training.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.enums.TrainingTypeName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsResponseDTO implements BaseDTO {
    private String trainingName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date trainingDate;

    private TrainingTypeName trainingTypeName;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal trainingDuration;

    private String trainerFirstName;
}
