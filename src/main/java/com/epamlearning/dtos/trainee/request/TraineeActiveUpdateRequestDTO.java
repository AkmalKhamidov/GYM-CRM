package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeActiveUpdateRequestDTO implements BaseDTO {
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;
}
