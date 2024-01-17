package com.epamlearning.dtos.training.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.enums.TrainingTypeName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequestDTO implements BaseDTO {

    @Schema(description = "Trainee username", example = "John.Wick", requiredMode = REQUIRED)
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Filter starting date for training date", example = "15-11-2023")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateFrom;

    @Schema(description = "Filter ending date for training date", example = "20-12-2023")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateTo;


    @Schema(description = "Trainee (first OR last) name", example = "John")
    private String trainerName;

    @Schema(description = "Training type name", example = "GYM_TYPE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TrainingTypeName trainingTypeName;
}
