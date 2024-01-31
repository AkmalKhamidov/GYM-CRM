package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.user.ProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class TrainerUpdateRequestDTO extends ProfileDTO implements BaseDTO {
    @Schema(description = "Trainer training type", example = "GYM_TYPE", accessMode = READ_ONLY)
    private String trainingTypeName;

    @Schema(description = "Trainer active status", example = "true", requiredMode = REQUIRED)
    @NotNull(message = "User active status cannot be null.")
    @BooleanFlag
    private boolean isActive;
}
