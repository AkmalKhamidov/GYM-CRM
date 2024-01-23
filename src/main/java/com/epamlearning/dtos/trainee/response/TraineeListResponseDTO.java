package com.epamlearning.dtos.trainee.response;

import com.epamlearning.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeListResponseDTO implements BaseDTO {
    private String username;
    private String firstName;
    private String lastName;
}
