package com.epamlearning.services;

import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService implements BaseService<User> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID: {} not found.", id);
            throw new NotFoundException("User with ID " + id + " not found.");
        }
        return user.get();
    }

    @Override
    public User findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.warn("User with username: {} not found.", username);
            throw new NotFoundException("User with username " + username + " not found.");
        }
        return user.get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }
        userNullVerification(user);

        User userToUpdate = findById(id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
//        userToUpdate.setUsername(user.getUsername());
//        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.isActive());
        return userRepository.save(userToUpdate);
    }

    public User updatePassword(String username, String oldPassword, String newPassword) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        if(oldPassword == null || oldPassword.isEmpty()) {
            log.warn("Old password is null.");
            throw new NullPointerException("Old password is null.");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            log.warn("New password is null.");
            throw new NullPointerException("New password is null.");
        }
        User userToUpdate = findByUsername(username);
        if(!userToUpdate.getPassword().equals(oldPassword)) {
            log.warn("Old password is wrong.");
            throw new NotFoundException("Old password is wrong.");
        }
        userToUpdate.setPassword(newPassword);
        return userRepository.save(userToUpdate);
    }

    public User updateActive(Long id, boolean active) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        User userToUpdate = findById(id);
        userToUpdate.setActive(active);
        return userRepository.save(userToUpdate);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        User userToDelete = findById(id);
        userRepository.delete(userToDelete);
    }



    public Long authenticate(String username, String password) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        User user = findByUsername(username);
        if (user.getPassword().equals(password)) {
            return user.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public User createUser(String firstName, String lastName) {

        if (firstName == null || firstName.isEmpty()) {
            log.warn("First name is null.");
            throw new NullPointerException("First name is null.");
        }
        if (lastName == null || lastName.isEmpty()) {
            log.warn("Last name is null.");
            throw new NullPointerException("Last name is null.");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(generateUserName(firstName, lastName));
        user.setPassword(generateRandomPassword());
        user.setActive(true);
        return user;
    }

    public String generateUserName(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;

        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        log.info("User's username generated: {}, firstName: {}, lastName: {}", username, firstName, lastName);
        return username;
    }

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public boolean userNullVerification(User user) {
        if (user == null) {
            log.warn("User is null.");
            throw new NullPointerException("User is null.");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            log.warn("First name is null.");
            throw new NullPointerException("First name is null.");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            log.warn("Last name is null.");
            throw new NullPointerException("Last name is null.");
        }
        return true;
    }

}
