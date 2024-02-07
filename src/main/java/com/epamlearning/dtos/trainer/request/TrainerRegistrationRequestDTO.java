package com.epamlearning.dtos.trainer.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationRequestDTO implements BaseDTO {
        @Schema(description = "Trainer first name", example = "John", requiredMode = REQUIRED)
        @NotNull(message = "User first name cannot be null.")
        @NotEmpty(message = "User first name cannot be blank.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        private String firstName;

        @Schema(description = "Trainer last name", example = "Smith", requiredMode = REQUIRED)
        @NotNull(message = "User last name cannot be null.")
        @NotEmpty(message = "User last name cannot be blank.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        private String lastName;

        @Schema(description = "Trainer training type", example = "GYM_TYPE", requiredMode = REQUIRED)
        @NotNull(message = "Training type cannot be null.")
        private Long specializationId;
}
