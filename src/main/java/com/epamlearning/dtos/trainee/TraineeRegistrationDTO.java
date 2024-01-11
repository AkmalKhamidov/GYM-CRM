package com.epamlearning.dtos.trainee;

import com.epamlearning.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationDTO implements BaseDTO {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
}
