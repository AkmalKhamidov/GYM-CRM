package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.models.TrainingType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationDTO implements BaseDTO {
    @NotNull(message = "User first name cannot be null.")
    @NotEmpty(message = "User first name cannot be blank.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @NotNull(message = "User last name cannot be null.")
    @NotEmpty(message = "User last name cannot be blank.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

    @NotNull(message = "Training type cannot be null.")
    private Long specializationId;
}
