package com.epamlearning.dtos.trainee.response;

import com.epamlearning.dtos.BaseDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TraineeListResponseDTO implements BaseDTO {
    private String username;
    private String firstName;
    private String lastName;
}
