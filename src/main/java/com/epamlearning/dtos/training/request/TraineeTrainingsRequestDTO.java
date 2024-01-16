package com.epamlearning.dtos.training.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.enums.TrainingTypeName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequestDTO implements BaseDTO {

    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateTo;

    private String trainerName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TrainingTypeName trainingTypeName;

}
