package com.epamlearning.controllers;

import com.epamlearning.dtos.training.request.TraineeTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainerTrainingsRequestDTO;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController implements BaseController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final Mapper mapper;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService, Mapper mapper) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.mapper = mapper;
    }

    @GetMapping("/by-trainee")
    public ResponseEntity<List<TraineeTrainingsResponseDTO>> getTraineeTrainings(@Validated @RequestBody TraineeTrainingsRequestDTO trainingDTO) {
        List<Training> trainings =
                trainingService.findByTraineeAndCriteria(
                        trainingDTO.getUsername(),
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        trainingTypeService.findByTrainingTypeName(trainingDTO.getTrainingTypeName()),
                        trainingDTO.getTrainerName()
                );
        List<TraineeTrainingsResponseDTO> responseDTO = trainings.stream().map(training -> mapper.mapToDTO(training, TraineeTrainingsResponseDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/by-trainer")
    public ResponseEntity<List<TrainerTrainingsResponseDTO>> getTrainerTrainings(@Validated @RequestBody TrainerTrainingsRequestDTO trainingDTO) {
        List<Training> trainings =
                trainingService.findByTrainerAndCriteria(
                        trainingDTO.getUsername(),
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        null,
                        trainingDTO.getTraineeName()
                );
        List<TrainerTrainingsResponseDTO> responseDTO = trainings.stream().map(training -> mapper.mapToDTO(training, TrainerTrainingsResponseDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

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
        return new ResponseEntity<>("Training added successfully.", HttpStatus.CREATED);
    }

}
