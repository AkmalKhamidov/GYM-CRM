package com.epamlearning.dtos.training.request;

import com.epamlearning.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingAddRequestDTO implements BaseDTO {

    @Schema(description = "Trainee username", example = "John.Wick", requiredMode = REQUIRED)
    @NotNull(message = "Trainee username cannot be null")
    @NotEmpty(message = "Trainee username cannot be empty")
    private String traineeUsername;

    @Schema(description = "Trainer username", example = "John.Smith", requiredMode = REQUIRED)
    @NotNull(message = "Trainer username cannot be null")
    @NotEmpty(message = "Trainer username cannot be empty")
    private String trainerUsername;

    @Schema(description = "Training name", example = "JavaXStack", requiredMode = REQUIRED)
    @NotNull(message = "Training name cannot be null")
    @NotEmpty(message = "Training name cannot be empty")
    @Size(min = 5, max = 50, message = "Training name must be between 5 and 50 characters.")
    private String trainingName;

    @Schema(description = "Training date (FUTURE/PRESENT DATE)", example = "2024-01-26", requiredMode = REQUIRED)
    @NotNull(message = "Training date cannot be null")
    @FutureOrPresent(message = "Training date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;

    @Schema(description = "Training duration", example = "60", requiredMode = REQUIRED)
    @NotNull(message = "Training type cannot be null")
    @Positive(message = "Training type must be positive")
    @Min(value = 5, message = "Training type must be greater than 5")
    @Max(value = 100, message = "Training type must be less than 100")
    private BigDecimal trainingDuration;

}
