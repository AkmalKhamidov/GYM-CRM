package com.epamlearning.services.impl;

import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.Training;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.exceptions.NotAuthorized;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TrainingMapper;
import com.epamlearning.repositories.TrainingRepository;
import com.epamlearning.services.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingMapper trainingMapper;
    private final TrainingRepository trainingRepository;
    private final TraineeServiceImpl traineeService;
    private final TrainerServiceImpl trainerService;

    @Autowired
    public TrainingServiceImpl(TrainingMapper trainingMapper, TrainingRepository trainingRepository, TraineeServiceImpl traineeService, TrainerServiceImpl trainerService) {
        this.trainingMapper = trainingMapper;
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public Training findById(Long id) {

        if (id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }

        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty()) {
            log.warn("Training with ID: {} not found.", id);
            throw new NotFoundException("Training with ID " + id + " not found.");
        }
        return training.get();
    }

    public boolean notExistsTraineeAndTrainerInAnotherTraining(Long trainingId, String traineeUsername, String trainerUsername) {
        Training training = findById(trainingId);
        Trainee trainee = traineeService.findByValidatedUsername(traineeUsername);
        Trainer trainer = trainerService.findByValidatedUsername(trainerUsername);
        return !trainingRepository.existsAnotherTrainingByTraineeAndTrainer(training, trainee, trainer);
    }

    public boolean existsTraineeAndTrainerInTrainings(String traineeUsername, String trainerUsername) {
        Trainee trainee = traineeService.findByValidatedUsername(traineeUsername);
        Trainer trainer = trainerService.findByValidatedUsername(trainerUsername);

        return trainingRepository.existsTrainingByTraineeAndTrainer(trainee, trainer);
    }


    public Training update(Long id, Training training) {

        if (training == null) {
            log.warn("Training is null.");
            throw new NullPointerException("Training is null.");
        }
        if (training.getTrainingDate() == null) {
            log.warn("StartDate is null.");
            throw new NullPointerException("StartDate is null.");
        }
        if (training.getTrainingDuration() == null) {
            log.warn("TrainingDuration is null.");
            throw new NullPointerException("TrainingDuration is null.");
        }

        Training trainingToUpdate = findById(id);
        trainingToUpdate.setTrainingType(training.getTrainingType());
        trainingToUpdate.setTrainingDate(training.getTrainingDate());
        trainingToUpdate.setTrainingDuration(training.getTrainingDuration());

        // check if new trainer is not the same as old one
        // check if old trainer is not in another training with trainee in order to delete it from trainee_trainer table
        if (!trainingToUpdate.getTrainer().getId().equals(training.getTrainer().getId())) {
            // get all trainers for trainee
            List<Trainer> traineeTrainers = new ArrayList<>(training.getTrainee().getTrainers());
            // add new trainer to list
            traineeTrainers.add(training.getTrainer());
            if (notExistsTraineeAndTrainerInAnotherTraining(id, trainingToUpdate.getTrainee().getUser().getUsername(), trainingToUpdate.getTrainer().getUser().getUsername())) {
                // distinct trainers (in case if new trainer is already in list) and remove old trainer
                traineeTrainers.stream().filter(trainer -> trainer.getId().equals(trainingToUpdate.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            }
            // update trainee with new list of trainers and set trainee to training
            trainingToUpdate.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getUser().getUsername(), traineeTrainers.stream().distinct().toList()));
        }
        trainingToUpdate.setTrainer(training.getTrainer());
        return trainingRepository.save(trainingToUpdate);
    }

    public void deleteById(Long id) {
        Training training = findById(id);
        if (notExistsTraineeAndTrainerInAnotherTraining(id, training.getTrainee().getUser().getUsername(), training.getTrainer().getUser().getUsername())) {
            List<Trainer> traineeTrainers = new ArrayList<>(training.getTrainee().getTrainers());
            traineeTrainers.stream().filter(trainer -> trainer.getId().equals(training.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getUser().getUsername(), traineeTrainers));
        }
        trainingRepository.delete(training);
    }

    public List<Training> findByTrainee(String username) {
        return trainingRepository.findByTrainee(traineeService.findByValidatedUsername(username));
    }

    @Transactional
    public void deleteTrainingsByTraineeUsername(String username) {
        findByTrainee(username).forEach(training -> {
            traineeService.updateTrainersForTrainee(training.getTrainee().getUser().getUsername(), new UpdateTrainersOfTraineeRequestDTO(new ArrayList<>()));
            trainingRepository.deleteByTrainee(training.getTrainee());
        });
    }

    @Override
    public List<TraineeTrainingsResponseDTO> findByTraineeAndCriteria(String traineeUsername, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String trainerName) {
        return trainingMapper.trainingsToTraineeTrainingsResponseDTOs(
                trainingRepository.findTrainingsByTraineeAndCriteria(
                        traineeService.findByValidatedUsername(traineeUsername),
                        dateFrom, dateTo, trainingType, trainerName));
    }

    @Override
    public List<TrainerTrainingsResponseDTO> findByTrainerAndCriteria(String trainerUsername, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String traineeName) {
        return trainingMapper.trainingsToTrainerTrainingsResponseDTOs(
                trainingRepository.findTrainingsByTrainerAndCriteria(
                        trainerService.findByValidatedUsername(trainerUsername),
                        dateFrom, dateTo, trainingType, traineeName));
    }

    @Override
    @Transactional
    public void createTraining(String trainingName,
                               LocalDate trainingDate,
                               BigDecimal trainingDuration,
                               String trainerUsername,
                               String traineeUsername) {

        if (trainingName == null || trainingName.isEmpty()) {
            log.warn("TrainingName is null.");
            throw new NullPointerException("TrainingName is null.");
        }
        if (trainingDate == null) {
            log.warn("TrainingDate is null.");
            throw new NullPointerException("TrainingDate is null.");
        }
        if (trainingDuration == null) {
            log.warn("TrainingDuration is null.");
            throw new NullPointerException("TrainingDuration is null.");
        }

//         Check if the authenticated user has the necessary roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isTrainer = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINER"));
        boolean isTrainee = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINEE"));

        // Check if the roles allow the operation
        if ((isTrainee && !isTrainer) || (!isTrainee && isTrainer)) {
            String authenticatedUsername = authentication.getName();
            if ((isTrainee && !traineeUsername.equals(authenticatedUsername)) ||
                    (isTrainer && !trainerUsername.equals(authenticatedUsername))) {
                throw new NotAuthorized("Access denied");
            }
        }

        Trainer trainer = trainerService.findByValidatedUsername(trainerUsername);
        Trainee trainee = traineeService.findByValidatedUsername(traineeUsername);

        Training training = new Training();
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainer.getSpecialization());

        if (!traineeService.hasTrainer(training.getTrainee().getUser().getUsername(), training.getTrainer().getUser().getUsername())
                && !existsTraineeAndTrainerInTrainings(training.getTrainee().getUser().getUsername(), training.getTrainer().getUser().getUsername())) {
            List<Trainer> traineeTrainers = new ArrayList<>(training.getTrainee().getTrainers());
            traineeTrainers.add(training.getTrainer());
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getUser().getUsername(), traineeTrainers));
        }
        trainingRepository.save(training);
    }

}
