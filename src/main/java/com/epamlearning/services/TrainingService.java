package com.epamlearning.services;

import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.Training;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.repositories.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TrainingService implements BaseService<Training> {

    private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }


    @Override
    public Training save(Training training) {
        if (training == null) {
            log.warn("Training is null.");
            throw new NullPointerException("Training is null.");
        }
        if (!traineeService.hasTrainer(training.getTrainee().getId(), training.getTrainer().getId())
                && !existsTraineeAndTrainerInTrainings(training.getTrainee(), training.getTrainer())) {
            List<Trainer> traineeTrainers = new ArrayList<>(training.getTrainee().getTrainers());
            traineeTrainers.add(training.getTrainer());
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers));
        }
        return trainingRepository.save(training);
    }

    @Override
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

    // No need to implementation (not used)
    @Override
    public Training findByUsername(String username) {
        return null;
    }

    public boolean notExistsTraineeAndTrainerInAnotherTraining(Long trainingId, Long traineeId, Long trainerId) {
        Training training = findById(trainingId);
        Trainee trainee = traineeService.findById(traineeId);
        Trainer trainer = trainerService.findById(trainerId);
        return !trainingRepository.existsAnotherTrainingByTraineeAndTrainer(training, trainee, trainer);
    }

    public boolean existsTraineeAndTrainerInTrainings(Trainee traineeId, Trainer trainerId) {
        Trainee trainee = traineeService.findById(traineeId.getId());
        Trainer trainer = trainerService.findById(trainerId.getId());

        return trainingRepository.existsTrainingByTraineeAndTrainer(trainee, trainer);
    }


    @Override
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
            if (notExistsTraineeAndTrainerInAnotherTraining(id, trainingToUpdate.getTrainee().getId(), trainingToUpdate.getTrainer().getId())) {
                // distinct trainers (in case if new trainer is already in list) and remove old trainer
                traineeTrainers.stream().filter(trainer -> trainer.getId().equals(trainingToUpdate.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            }
            // update trainee with new list of trainers and set trainee to training
            trainingToUpdate.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers.stream().distinct().toList()));
        }
        trainingToUpdate.setTrainer(training.getTrainer());
        return trainingRepository.save(trainingToUpdate);
    }


    @Override
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Training training = findById(id);
        if (notExistsTraineeAndTrainerInAnotherTraining(id, training.getTrainee().getId(), training.getTrainer().getId())) {
            List<Trainer> traineeTrainers = new ArrayList<>(training.getTrainee().getTrainers());
            traineeTrainers.stream().filter(trainer -> trainer.getId().equals(training.getTrainer().getId())).findFirst().ifPresent(traineeTrainers::remove);
            training.setTrainee(traineeService.updateTrainersForTrainee(training.getTrainee().getId(), traineeTrainers));
        }
        trainingRepository.delete(training);
    }

    public List<Training> findByTraineeAndCriteria(String username, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String trainerName) {
        Trainee trainee = traineeService.findByUsername(username);
        return trainingRepository.findTrainingsByTraineeAndCriteria(trainee, dateFrom, dateTo, trainingType, trainerName);
    }

    public List<Training> findByTrainee(Long traineeId) {
        return trainingRepository.findByTrainee(traineeService.findById(traineeId));
    }

    public void deleteTrainingsByTraineeId(Long traineeId) {
        findByTrainee(traineeId).forEach(training -> {
            traineeService.updateTrainersForTrainee(training.getTrainee().getId(), new ArrayList<>());
            trainingRepository.deleteByTrainee(training.getTrainee());
        });
    }


    public List<Training> findByTrainerAndCriteria(String trainerUsername, LocalDate dateFrom, LocalDate
            dateTo, TrainingType trainingType, String traineeName) {
        return trainingRepository.findTrainingsByTrainerAndCriteria(trainerService.findByUsername(trainerUsername), dateFrom, dateTo, trainingType, traineeName);
    }

    public Training createTraining(String trainingName, LocalDate trainingDate, BigDecimal trainingDuration, Trainer
            trainer, Trainee trainee, TrainingType trainingType) {
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

        Training training = new Training();
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        return training;
    }

}
