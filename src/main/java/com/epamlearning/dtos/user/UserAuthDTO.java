package com.epamlearning.dtos.user;

import com.epamlearning.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO implements BaseDTO {
    private String username;
    private String password;
}
