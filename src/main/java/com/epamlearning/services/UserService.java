package com.epamlearning.services;

import com.epamlearning.dtos.SessionDTO;

public interface UserService extends BaseService{
    void updatePassword(String username, String oldPassword, String newPassword);
    SessionDTO authenticate(String username, String password);
}
