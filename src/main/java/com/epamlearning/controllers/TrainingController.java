package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.training.request.TraineeTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainerTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.entities.Training;
import com.epamlearning.mapper.TrainingMapper;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
@Tag(name = "Training Controller", description = "Controller for managing trainings")
public class TrainingController implements BaseController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingMapper trainingMapper;
    private final TrainingTypeService trainingTypeService;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService, TrainerService trainerService,
                              TrainingMapper trainingMapper, TrainingTypeService trainingTypeService, UserEngagementMetrics metrics) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingMapper = trainingMapper;
        this.trainingTypeService = trainingTypeService;
        this.metrics = metrics;
    }

    @Operation(summary = "Get trainee trainings", description = "Getting trainee trainings by username and optional criteria")
    @GetMapping("/by-trainee")
    public ResponseEntity<List<TraineeTrainingsResponseDTO>> getTraineeTrainings(@Validated TraineeTrainingsRequestDTO trainingDTO) {
        List<Training> trainings =
                trainingService.findByTraineeAndCriteria(
                        trainingDTO.getUsername(),
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        trainingTypeService.findByTrainingTypeName(trainingDTO.getTrainingTypeName()),
                        StringUtils.trimToNull(trainingDTO.getTrainerName())
                );
        List<TraineeTrainingsResponseDTO> responseDTO = trainingMapper.trainingsToTraineeTrainingsResponseDTOs(trainings);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get trainer trainings", description = "Getting trainer trainings by username and optional criteria")
    @GetMapping("/by-trainer")
    public ResponseEntity<List<TrainerTrainingsResponseDTO>> getTrainerTrainings(@Validated TrainerTrainingsRequestDTO trainingDTO) {
        List<Training> trainings =
                trainingService.findByTrainerAndCriteria(
                        trainingDTO.getUsername(),
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        null,
                        StringUtils.trimToNull(trainingDTO.getTraineeName())
                );
        List<TrainerTrainingsResponseDTO> responseDTO = trainingMapper.trainingsToTrainerTrainingsResponseDTOs(trainings);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Add training", description = "Adding training with trainer training type")
    @PostMapping("/add")
    public ResponseEntity<String> addTraining(@Validated @RequestBody TrainingAddRequestDTO trainingAddDTO) {
        Trainee trainee = traineeService.findByUsername(trainingAddDTO.getTraineeUsername());
        Trainer trainer = trainerService.findByUsername(trainingAddDTO.getTrainerUsername());
        Training training = trainingService.createTraining(
                trainingAddDTO.getTrainingName(),
                trainingAddDTO.getTrainingDate(),
                trainingAddDTO.getTrainingDuration(),
                trainer,
                trainee,
                trainer.getSpecialization()
        );
        Training savedTraining = trainingService.save(training);

        metrics.registerNewTraining();

        return new ResponseEntity<>("Training added successfully.", HttpStatus.CREATED);
    }

}
