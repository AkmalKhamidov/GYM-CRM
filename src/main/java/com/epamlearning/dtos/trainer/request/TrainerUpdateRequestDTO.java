package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateRequestDTO implements BaseDTO {
    @Schema(description = "Trainer username", example = "John.Smith", requiredMode = REQUIRED)
    @NotNull(message = "User first name cannot be null.")
    @NotEmpty(message = "User first name cannot be empty.")
    private String username;

    @Schema(description = "Trainer first name", example = "John", requiredMode = REQUIRED)
    @NotNull(message = "User first name cannot be null.")
    @NotEmpty(message = "User first name cannot be empty.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstName;

    @Schema(description = "Trainer last name", example = "Smith", requiredMode = REQUIRED)
    @NotNull(message = "User last name cannot be null.")
    @NotEmpty(message = "User last name cannot be empty.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastName;

//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Trainer training type", example = "GYM_TYPE", accessMode = READ_ONLY)
    private String trainingTypeName;

    @Schema(description = "Trainer active status", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "User active status cannot be null.")
    @BooleanFlag
    private boolean isActive;
}
