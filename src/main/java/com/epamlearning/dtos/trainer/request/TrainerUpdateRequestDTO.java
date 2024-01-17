package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateRequestDTO implements BaseDTO {
    @NotNull(message = "User first name cannot be null.")
    @NotEmpty(message = "User first name cannot be empty.")
    private String username;

    @NotNull(message = "User first name cannot be null.")
    @NotEmpty(message = "User first name cannot be empty.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @NotNull(message = "User last name cannot be null.")
    @NotEmpty(message = "User last name cannot be empty.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String trainingTypeName;

    @NotNull(message = "User active status cannot be null.")
    @BooleanFlag
    private boolean isActive;
}
