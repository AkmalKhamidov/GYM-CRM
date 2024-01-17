package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record TraineeRegistrationDTO(
        @NotNull(message = "User first name cannot be null.")
        @NotEmpty(message = "User first name cannot be empty.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        String firstName,

        @NotNull(message = "User last name cannot be null.")
        @NotEmpty(message = "User last name cannot be empty.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        String lastName,

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        @Past(message = "Date of birth cannot be in the future.")
        Date dateOfBirth,
        String address) implements BaseDTO {
}
