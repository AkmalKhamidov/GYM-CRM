package com.epamlearning.dtos.trainer.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainerActiveUpdateRequestDTO implements BaseDTO {
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;
}
