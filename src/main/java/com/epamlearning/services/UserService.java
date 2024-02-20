package com.epamlearning.services;

import com.epamlearning.dtos.SessionDTO;
import com.epamlearning.dtos.user.RefreshRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService extends BaseService{
    void updatePassword(String username, String oldPassword, String newPassword);
    SessionDTO authenticate(String username, String password);

    void logout(HttpServletRequest request, HttpServletResponse response);

    SessionDTO refreshToken(RefreshRequestDTO refreshRequestDTO);
}
