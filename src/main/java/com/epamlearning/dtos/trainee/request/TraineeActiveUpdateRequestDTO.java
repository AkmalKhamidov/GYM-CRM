package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TraineeActiveUpdateRequestDTO implements BaseDTO {
    @Schema(description = "Username", example = "John.Smith", required = true)
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;


    @NotNull(message = "Active status cannot be null")
    private boolean isActive;
}
