package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainersOfTraineeRequestDTO implements BaseDTO {

    @Schema(description = "Username", example = "John.Wick", requiredMode = REQUIRED)
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Schema(description = "List of trainers", example = "[{\"username\":\"John.Smith\"},{\"username\":\"James.Bond\"}]", requiredMode = REQUIRED)
    private List<TrainerUsernameRequestDTO> trainers;

}
