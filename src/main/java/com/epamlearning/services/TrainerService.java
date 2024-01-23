package com.epamlearning.services;

import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainerService implements BaseService<Trainer> {

    private final TrainerRepository trainerRepository;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TraineeService traineeService, TrainingTypeService trainingTypeService, UserService userService) {
        this.trainerRepository = trainerRepository;
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
    }

    @Override
    public Trainer findById(Long id) {
        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Trainer> trainer = trainerRepository.findById(id);
        if (trainer.isEmpty()) {
            log.warn("Trainer with ID: {} not found.", id);
            throw new NotFoundException("Trainer with ID " + id + " not found.");
        }
        return trainer.get();
    }

    @Override
    public Trainer findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NullPointerException("Username is null.");
        }

        Optional<Trainer> trainer = trainerRepository.findTrainerByUserUsername(username);
        if (trainer.isEmpty()) {
            log.warn("Trainer with username: {} not found.", username);
            throw new NotFoundException("Trainer with username " + username + " not found.");
        }
        return trainer.get();
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        if (trainer == null) {
            log.warn("Trainer is null.");
            throw new NullPointerException("Trainer is null.");
        }
        if (trainer.getSpecialization() == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }
        userService.userNullVerification(trainer.getUser());
        Trainer trainerToUpdate = findById(id);
        trainerToUpdate.getUser().setFirstName(trainer.getUser().getFirstName());
        trainerToUpdate.getUser().setLastName(trainer.getUser().getLastName());
        trainerToUpdate.getUser().setActive(trainer.getUser().isActive());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        return trainerRepository.save(trainerToUpdate);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        trainerRepository.delete(findById(id));
    }

    public Long authenticate(String username, String password) {
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
        Trainer trainer = findByUsername(username);
        if (trainer.getUser().getPassword().equals(password)) {
            return trainer.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    public Trainer updateActive(Long id, boolean active) {
        Trainer trainerUpdated = findById(id);
        trainerUpdated.getUser().setActive(active);
        return trainerRepository.save(trainerUpdated);
    }

    public Trainer updatePassword(Long id, String password) {
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
        Trainer trainerUpdated = findById(id);
        trainerUpdated.getUser().setPassword(password);
        return trainerRepository.save(trainerUpdated);
    }

    public List<Trainer> findNotAssignedActiveTrainers(String username) {
        return trainerRepository.findNotAssignedActiveTrainersOfTrainee(traineeService.findByUsername(username));
    }

    public Trainer createTrainer(String firstName, String lastName, Long trainingTypeId) {
        if (trainingTypeId == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }
        User user = userService.createUser(firstName, lastName);
        TrainingType trainingType = trainingTypeService.findById(trainingTypeId);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        return trainer;
    }
}
