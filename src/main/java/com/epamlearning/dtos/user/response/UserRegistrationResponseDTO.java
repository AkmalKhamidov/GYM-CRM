package com.epamlearning.dtos.user.response;

import com.epamlearning.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationResponseDTO implements BaseDTO {
    private String username;
    private String password;
}
