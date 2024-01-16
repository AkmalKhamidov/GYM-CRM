package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrainerRegistrationDTO(
        @NotNull(message = "User first name cannot be null.")
        @NotEmpty(message = "User first name cannot be blank.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        String firstName,

        @NotNull(message = "User last name cannot be null.")
        @NotEmpty(message = "User last name cannot be blank.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        String lastName,

        @NotNull(message = "Training type cannot be null.")
        Long specializationId) implements BaseDTO {
}
