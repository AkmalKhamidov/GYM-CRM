package com.epamlearning.security;

import com.epamlearning.entities.User;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogoutUser {
    private User user;
    private Date logoutTime;
}
