package com.epamlearning.dtos.training.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequestDTO implements BaseDTO {

    @Schema(description = "Filter starting date for training date", example = "2023-11-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @Schema(description = "Filter ending date for training date", example = "2023-12-20")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    @Schema(description = "Trainee (first OR last) name", example = "John")
    private String trainerName;

    @Schema(description = "Training type name", example = "GYM_TYPE")
    private TrainingTypeName trainingTypeName;
}
