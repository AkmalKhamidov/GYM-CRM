package com.epamlearning.services.impl;

import com.epamlearning.entities.LoginAttempt;
import com.epamlearning.repositories.LoginAttemptRepository;
import com.epamlearning.services.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptServiceImpl implements LoginAttemptService {

  @Value("${login_attempts.max_attempts}")
  private int max_attempts;

  @Value("${login_attempts.lock_duration}")
  private long lock_duration;

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
    log.info("User with username: " + username + " login failed.");
    loginAttemptRepository.save(loginAttempt);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isBlocked(String username) {
    List<LoginAttempt> attempts = loginAttemptRepository.findByUsername(username);
    if (attempts.size() >= max_attempts && attempts.size() % max_attempts == 0) {
      LocalDateTime lastAttemptTime = attempts.get(attempts.size() - 1).getTimestamp();
      return lastAttemptTime.plusMinutes(TimeUnit.MILLISECONDS.toMinutes(lock_duration))
          .isAfter(LocalDateTime.now());
    }
    return false;
  }

  @Override
  @Transactional
  public void loginSucceeded(String username) {
    loginAttemptRepository.deleteByUsername(username);
  }
}
