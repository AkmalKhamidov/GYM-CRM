package com.epamlearning.services.impl;

import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.entities.Role;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.User;
import com.epamlearning.entities.enums.RoleName;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.repositories.TrainerRepository;
import com.epamlearning.services.RoleService;
import com.epamlearning.services.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional(readOnly = true)
public class TrainerServiceImpl implements TrainerService {

    private final TrainerMapper trainerMapper;

    private final TrainerRepository trainerRepository;
    private final TraineeServiceImpl traineeServiceImpl;
    private final TrainingTypeServiceImpl trainingTypeServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    @Autowired
    public TrainerServiceImpl(TrainerMapper trainerMapper, TrainerRepository trainerRepository, TraineeServiceImpl traineeServiceImpl, TrainingTypeServiceImpl trainingTypeServiceImpl, UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.trainerMapper = trainerMapper;
        this.trainerRepository = trainerRepository;
        this.traineeServiceImpl = traineeServiceImpl;
        this.trainingTypeServiceImpl = trainingTypeServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public TrainerProfileResponseDTO findByUsername(String username) {
        return trainerMapper.trainerToTrainerProfileResponseDTO(findByValidatedUsername(username));
    }

    public Trainer findByValidatedUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null.");
            throw new NotFoundException("Username is null.");
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

    @Transactional
    @Override
    public TrainerProfileResponseDTO updateActive(String username, boolean active) {
        Trainer trainerUpdated = findByValidatedUsername(username);
        trainerUpdated.getUser().setActive(active);
        return trainerMapper.trainerToTrainerProfileResponseDTO(trainerRepository.save(trainerUpdated));
    }

    @Override
    public List<TrainerListResponseDTO> findNotAssignedActiveTrainers(String username) {
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
            throw new NotFoundException("TrainingType is null.");
        }
        User user = userServiceImpl.createUser(firstName, lastName);
        String initialPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        TrainingType trainingType = trainingTypeServiceImpl.findById(trainingTypeId);
        Role role_trainer = roleService.findByRoleName(RoleName.ROLE_TRAINER);
        user.setRoles(Set.of(role_trainer));
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        trainerRepository.save(trainer);
        return new TrainerRegistrationResponseDTO(user.getUsername(), initialPassword);
    }
}
