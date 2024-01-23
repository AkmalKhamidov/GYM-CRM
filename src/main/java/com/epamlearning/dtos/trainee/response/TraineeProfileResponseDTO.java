package com.epamlearning.dtos.trainee.response;

import com.epamlearning.dtos.BaseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileResponseDTO implements BaseDTO {
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerListResponseDTO> trainerList;
}
