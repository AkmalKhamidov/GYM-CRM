package com.epamlearning.services;

import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional(readOnly = true)
public class TraineeService implements BaseService<Trainee> {

    private final TraineeRepository traineeRepository;
    private final UserService userService;

    @Autowired
    @Lazy
    private TrainingService trainingService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserService userService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
    }

    @Override
    public Trainee findById(Long id) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Trainee> trainee = traineeRepository.findById(id);
        if (trainee.isEmpty()) {
            log.warn("Trainee with ID: {} not found.", id);
            throw new NotFoundException("Trainee with ID " + id + " not found.");
        }
        return trainee.get();
    }

    @Override
    public List<Trainee> findAll() {
        return traineeRepository.findAll();
    }

    @Override
    public Trainee findByUsername(String username) {

        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }

        Optional<Trainee> trainee = traineeRepository.findTraineeByUserUsername(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username: {} not found.", username);
            throw new NotFoundException("Trainee with username " + username + " not found.");
        }
        return trainee.get();
    }

    @Transactional
    @Override
    public Trainee save(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    @Transactional
    @Override
    public Trainee update(Long id, Trainee trainee) {
        if (trainee == null) {
            log.warn("Trainee is null.");
            throw new NullPointerException("Trainee is null.");
        }
        userService.userNullVerification(trainee.getUser());

        Trainee traineeToUpdate = findById(id);
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.getUser().setFirstName(trainee.getUser().getFirstName());
        traineeToUpdate.getUser().setLastName(trainee.getUser().getLastName());
        traineeToUpdate.getUser().setActive(trainee.getUser().isActive());
        return traineeRepository.save(traineeToUpdate);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Trainee trainee = findById(id);
        trainingService.deleteTrainingsByTraineeId(trainee.getId());
        traineeRepository.delete(trainee);
    }

    @Transactional
    public void deleteByUsername(String username) {
        Trainee trainee = findByUsername(username);
        trainingService.deleteTrainingsByTraineeId(trainee.getId());
        traineeRepository.delete(trainee);
    }

    public Long authenticate(String username, String password) {

        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }

        Trainee trainee = findByUsername(username);
        if (trainee.getUser().getPassword().equals(password)) {
            return trainee.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    @Transactional
    public Trainee updateActive(Long id, boolean active) {
        Trainee traineeUpdated = findById(id);
        traineeUpdated.getUser().setActive(active);
        return traineeRepository.save(traineeUpdated);
    }

    @Transactional
    public Trainee updateTrainersForTrainee(Long traineeId, List<Trainer> trainers) {

        Trainee traineeToUpdate = findById(traineeId);
        traineeToUpdate.getTrainers().clear();
        traineeToUpdate.getTrainers().addAll(trainers);
//        traineeToUpdate.setTrainers(trainers);

        // Save the updated trainee
        return traineeRepository.save(traineeToUpdate);
    }

    public boolean hasTrainer(Long traineeId, Long trainerId) {
        List<Trainer> traineeTrainer = findById(traineeId).getTrainers();
        if(traineeTrainer.stream().filter(trainer -> trainer.getId().equals(trainerId)).toList().isEmpty()) {
            log.info("Trainee with ID: {} has no trainer with ID: {}.", traineeId, trainerId);
            return false;
        } else {
            log.info("Trainee with ID: {} has no trainer with ID: {}.", traineeId, trainerId);
            return true;
        }
    }

    public Trainee createTrainee(String firstName, String lastName, String address, Date dateOfBirth) {
        User user = userService.createUser(firstName, lastName);
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setUser(user);
        return trainee;
    }

}
