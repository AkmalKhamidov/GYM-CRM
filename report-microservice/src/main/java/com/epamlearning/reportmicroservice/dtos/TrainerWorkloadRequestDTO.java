package com.epamlearning.reportmicroservice.dtos;

import com.epamlearning.reportmicroservice.entities.enums.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainerWorkloadRequestDTO {

    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private LocalDate trainingDate;

    private BigDecimal trainingDuration;

    @NotNull(message = "Action type can not be null")
    private ActionType actionType;

    @NotNull(message = "Trainee username can not be null")
    @NotBlank(message = "Trainee username can not be blank")
    private String traineeUsername;
}
