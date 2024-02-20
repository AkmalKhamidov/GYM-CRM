package com.epamlearning.services;

public interface LoginAttemptService extends BaseService{
    void loginFailed(String username);
    boolean isBlocked(String username);

    void loginSucceeded(String username);
}
