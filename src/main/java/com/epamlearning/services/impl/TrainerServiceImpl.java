package com.epamlearning.services.impl;

import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.User;
import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.repositories.TrainerRepository;
import com.epamlearning.services.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerRepository trainerRepository;
    private final TraineeServiceImpl traineeServiceImpl;
    private final AuthorizationServiceImpl authService;
    private final TrainingTypeServiceImpl trainingTypeServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public TrainerServiceImpl(TrainerMapper trainerMapper, TrainerRepository trainerRepository, TraineeServiceImpl traineeServiceImpl, AuthorizationServiceImpl authService, TrainingTypeServiceImpl trainingTypeServiceImpl, UserServiceImpl userServiceImpl) {
        this.trainerMapper = trainerMapper;
        this.trainerRepository = trainerRepository;
        this.traineeServiceImpl = traineeServiceImpl;
        this.authService = authService;
        this.trainingTypeServiceImpl = trainingTypeServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public TrainerProfileResponseDTO findByUsername(String username) {
        authService.authorizeUser(username);
        return trainerMapper.trainerToTrainerProfileResponseDTO(findByValidatedUsername(username));
    }

    public Trainer findByValidatedUsername(String username) {
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

    @Transactional
    @Override
    public TrainerProfileResponseDTO update(String username, TrainerUpdateRequestDTO dto) {
        authService.authorizeUser(username);
        userServiceImpl.userNullVerification(dto);
        Trainer trainerToUpdate = findByValidatedUsername(username);
        trainerToUpdate.getUser().setFirstName(dto.getFirstName());
        trainerToUpdate.getUser().setLastName(dto.getLastName());
        trainerToUpdate.getUser().setActive(dto.isActive());
        return trainerMapper.trainerToTrainerProfileResponseDTO(trainerRepository.save(trainerToUpdate));
    }

    @Override
    public List<TrainerProfileResponseDTO> findAll() {
        return trainerMapper.trainersToTrainerProfileResponseDTOs(trainerRepository.findAll());
    }

    public Long authenticate(String username, String password) {
        if (password == null || password.isEmpty()) {
            log.warn("Password is null.");
            throw new NullPointerException("Password is null.");
        }
        Trainer trainer = findByValidatedUsername(username);
        if (trainer.getUser().getPassword().equals(password)) {
            return trainer.getId();
        } else {
            log.warn("Wrong password. Username: {} ", username);
            throw new NotAuthenticated("Wrong password. Username: " + username);
        }
    }

    @Transactional
    @Override
    public TrainerProfileResponseDTO updateActive(String username, boolean active) {
        authService.authorizeUser(username);
        Trainer trainerUpdated = findByValidatedUsername(username);
        trainerUpdated.getUser().setActive(active);
        return trainerMapper.trainerToTrainerProfileResponseDTO(trainerRepository.save(trainerUpdated));
    }

    @Override
    public List<TrainerListResponseDTO> findNotAssignedActiveTrainers(String username) {
        authService.authorizeUser(username);
        return trainerMapper.trainersToTrainerListResponseDTOs(
                trainerRepository.findNotAssignedActiveTrainersOfTrainee(
                        traineeServiceImpl.findByValidatedUsername(username)
                )
        );
    }

    @Transactional
    @Override
    public TrainerRegistrationResponseDTO createTrainer(String firstName, String lastName, Long trainingTypeId) {
        if (trainingTypeId == null) {
            log.warn("TrainingType is null.");
            throw new NullPointerException("TrainingType is null.");
        }
        User user = userServiceImpl.createUser(firstName, lastName);
        TrainingType trainingType = trainingTypeServiceImpl.findById(trainingTypeId);
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        return trainerMapper.trainerToTrainerRegistrationResponseDTO(trainerRepository.save(trainer));
    }
}
