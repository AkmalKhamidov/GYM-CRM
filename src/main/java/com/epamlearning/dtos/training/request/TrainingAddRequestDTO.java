package com.epamlearning.dtos.training.request;

import com.epamlearning.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingAddRequestDTO implements BaseDTO {

    @NotNull(message = "Trainee username cannot be null")
    @NotEmpty(message = "Trainee username cannot be empty")
    private String traineeUsername;

    @NotNull(message = "Trainer username cannot be null")
    @NotEmpty(message = "Trainer username cannot be empty")
    private String trainerUsername;

    @NotNull(message = "Training name cannot be null")
    @NotEmpty(message = "Training name cannot be empty")
    @Size(min = 5, max = 50, message = "Training name must be between 5 and 50 characters.")
    private String trainingName;

    @NotNull(message = "Training date cannot be null")
    @FutureOrPresent(message = "Training date cannot be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date trainingDate;

    @NotNull(message = "Training type cannot be null")
    @Positive(message = "Training type must be positive")
    @Min(value = 5, message = "Training type must be greater than 5")
    @Max(value = 100, message = "Training type must be less than 100")
    private BigDecimal trainingDuration;

}
