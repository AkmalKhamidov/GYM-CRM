package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
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
public class TraineeUpdateRequestDTO implements BaseDTO {

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

    @Schema(description = "Trainee date of birth", example = "2000-01-02")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Schema(description = "Trainee address", example = "Some address")
    private String address;

    @Schema(description = "Trainee active status", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "User active status cannot be null.")
    @BooleanFlag
    private boolean isActive;
}
