package com.epamlearning.dtos.trainee.request;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainersUpdateDTO implements BaseDTO {

    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    private List<TrainerUsernameRequestDTO> trainers;

}
