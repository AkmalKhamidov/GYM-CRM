package com.epamlearning.services.impl;

import com.epamlearning.entities.LoginAttempt;
import com.epamlearning.repositories.LoginAttemptRepository;
import com.epamlearning.services.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService
{

    @Value("${login_attempts.max_attempts}")
    private int MAX_ATTEMPTS;

    @Value("${login_attempts.lock_duration}")
    private long LOCK_DURATION;

    private final LoginAttemptRepository loginAttemptRepository;

    @Autowired
    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    @Override
    @Transactional
    public void loginFailed(String username) {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUsername(username);
        loginAttempt.setTimestamp(LocalDateTime.now());
        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlocked(String username) {
        List<LoginAttempt> attempts = loginAttemptRepository.findByUsername(username);
        if (attempts.size() >= MAX_ATTEMPTS && attempts.size() % MAX_ATTEMPTS == 0) {
            LocalDateTime lastAttemptTime = attempts.get(attempts.size() - 1).getTimestamp();
            if (lastAttemptTime.plusMinutes(TimeUnit.MILLISECONDS.toMinutes(LOCK_DURATION)).isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void loginSucceeded(String username) {
        loginAttemptRepository.deleteByUsername(username);
    }
}
