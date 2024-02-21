package com.epamlearning.services.impl;

import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.entities.Role;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.User;
import com.epamlearning.entities.enums.RoleName;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TraineeMapper;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.microservices.report.ActionType;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import com.epamlearning.repositories.TraineeRepository;
import com.epamlearning.services.RoleService;
import com.epamlearning.services.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
@Transactional(readOnly = true)
public class TraineeServiceImpl implements TraineeService {

    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TraineeRepository traineeRepository;
    private final UserServiceImpl userService;
    private final TrainerServiceImpl trainerService;
    private final TrainingServiceImpl trainingService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TraineeServiceImpl(TraineeMapper traineeMapper, TrainerMapper trainerMapper,
                              TraineeRepository traineeRepository,
                              UserServiceImpl userService,
                              @Lazy TrainerServiceImpl trainerService,
                              @Lazy TrainingServiceImpl trainingService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<TraineeProfileResponseDTO> findAll() {
        return traineeMapper.traineesToTraineeProfileResponseToDTOs(traineeRepository.findAll());
    }

    @Override
    public TraineeProfileResponseDTO findByUsername(String username) {
        return traineeMapper.traineeToTraineeProfileResponseToDTO(findByValidatedUsername(username));
    }

    public Trainee findByValidatedUsername(String username) {

        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NotFoundException("Username is null.");
        }

        Optional<Trainee> trainee = traineeRepository.findTraineeByUserUsername(username);
        if (trainee.isEmpty()) {
            log.warn("Trainee with username: {} not found.", username);
            throw new NotFoundException("Trainee with username " + username + " not found.");
        }
        return trainee.get();
    }

    @Override
    @Transactional
    public TraineeProfileResponseDTO update(String username, TraineeUpdateRequestDTO dto) {
        userService.userNullVerification(dto);
        Trainee traineeToUpdate = findByValidatedUsername(username);
        traineeToUpdate.setDateOfBirth(dto.getDateOfBirth());
        traineeToUpdate.setAddress(dto.getAddress());
        traineeToUpdate.getUser().setFirstName(dto.getFirstName());
        traineeToUpdate.getUser().setLastName(dto.getLastName());
        traineeToUpdate.getUser().setActive(dto.isActive());
        Trainee updatedTrainee = traineeRepository.save(traineeToUpdate);
        log.info("Trainee with username: {} was updated. updated trainee: {}", username, updatedTrainee);
        return traineeMapper.traineeToTraineeProfileResponseToDTO(updatedTrainee);
    }

    @Override
    @Transactional
    public TrainerWorkloadDTO deleteByUsername(String username) {
        Trainee trainee = findByValidatedUsername(username);
        trainingService.deleteTrainingsByTraineeUsername(trainee.getUser().getUsername());
        traineeRepository.delete(trainee);
        log.info("Trainee with username: {} was deleted.", username);
        TrainerWorkloadDTO trainerWorkloadDTO = new TrainerWorkloadDTO();
        trainerWorkloadDTO.setTraineeUsername(username);
        trainerWorkloadDTO.setActionType(ActionType.DELETE);
        return trainerWorkloadDTO;
    }

    @Override
    @Transactional
    public TraineeProfileResponseDTO updateActive(String username, boolean active) {
        Trainee traineeUpdated = findByValidatedUsername(username);
        traineeUpdated.getUser().setActive(active);
        log.info("Trainee with username: {} was {}.", username, active ? "activated" : "deactivated");
        return traineeMapper.traineeToTraineeProfileResponseToDTO(traineeRepository.save(traineeUpdated));
    }

    @Transactional
    public Trainee updateTrainersForTrainee(String username, List<Trainer> trainers) {
        Trainee traineeToUpdate = findByValidatedUsername(username);
        traineeToUpdate.getTrainers().clear();
        traineeToUpdate.getTrainers().addAll(trainers);
        Trainee updatedTrainee = traineeRepository.save(traineeToUpdate);
        log.warn("Trainers of trainee with username: {} were updated. trainers list: {}", username, updatedTrainee.getTrainers());
        return updatedTrainee;
    }

    @Override
    @Transactional
    public List<TrainerListResponseDTO> updateTrainersForTrainee(String username, UpdateTrainersOfTraineeRequestDTO dto) {

        Trainee traineeToUpdate = findByValidatedUsername(username);
        List<Trainer> trainers = dto.getTrainers().stream().map(trainer -> trainerService.findByValidatedUsername(trainer.getUsername())).toList();
        traineeToUpdate.getTrainers().clear();
        traineeToUpdate.getTrainers().addAll(trainers);
        Trainee updatedTrainee = traineeRepository.save(traineeToUpdate);
        log.warn("Trainers of trainee with username: {} were updated. trainers list: {}", username, updatedTrainee.getTrainers());
        return trainerMapper.trainersToTrainerListResponseDTOs(updatedTrainee.getTrainers());
    }

    public boolean hasTrainer(String traineeUsername, String trainerUsername) {
        List<Trainer> traineeTrainer = findByValidatedUsername(traineeUsername).getTrainers();
        if(traineeTrainer.stream().filter(trainer -> trainer.getUser().getUsername().equals(trainerUsername)).toList().isEmpty()) {
            log.info("Trainee with username: {} has no trainer with username: {}.", traineeUsername, trainerUsername);
            return false;
        } else {
            log.info("Trainee with username: {} has trainer with username: {}.", traineeUsername, trainerUsername);
            return true;
        }
    }
    @Override
    @Transactional
    public TraineeRegistrationResponseDTO createTrainee(String firstName, String lastName, String address, Date dateOfBirth) {
        User user = userService.createUser(firstName, lastName);
        String initialPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role_trainee = roleService.findByRoleName(RoleName.ROLE_TRAINEE);
        user.setRoles(Set.of(role_trainee));
        Trainee trainee = new Trainee();
        trainee.setAddress(address);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setUser(user);
        traineeRepository.save(trainee);
        return new TraineeRegistrationResponseDTO(user.getUsername(), initialPassword);
    }

}
