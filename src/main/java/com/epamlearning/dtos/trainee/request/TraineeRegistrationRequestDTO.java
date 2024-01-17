package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequestDTO
         implements BaseDTO {

        @Schema(description = "Trainee first name", example = "John", requiredMode = REQUIRED)
        @NotNull(message = "User first name cannot be null.")
        @NotEmpty(message = "User first name cannot be empty.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        private String firstName;

        @Schema(description = "Trainee last name", example = "Wick", requiredMode = REQUIRED)
        @NotNull(message = "User last name cannot be null.")
        @NotEmpty(message = "User last name cannot be empty.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        private String lastName;

        @Schema(description = "Trainee date of birth", example = "01-01-2000")
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        @Past(message = "Date of birth cannot be in the future.")
        private Date dateOfBirth;

        @Schema(description = "Trainee address", example = "Some address")
        private String address;

}
