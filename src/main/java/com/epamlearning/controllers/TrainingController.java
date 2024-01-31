package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.services.impl.TrainingServiceImpl;
import com.epamlearning.services.impl.TrainingTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/training")
@Tag(name = "Training Controller", description = "Controller for managing trainings")
public class TrainingController implements BaseController {

    private final TrainingServiceImpl trainingServiceImpl;
    private final TrainingTypeServiceImpl trainingTypeServiceImpl;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TrainingController(TrainingServiceImpl trainingServiceImpl, TrainingTypeServiceImpl trainingTypeServiceImpl, UserEngagementMetrics metrics) {
        this.trainingServiceImpl = trainingServiceImpl;
        this.trainingTypeServiceImpl = trainingTypeServiceImpl;
        this.metrics = metrics;
    }



    @Operation(summary = "Add training", description = "Adding training with trainer training type")
    @PostMapping
    public ResponseEntity<String> addTraining(@Validated @RequestBody TrainingAddRequestDTO trainingAddDTO) {
        trainingServiceImpl.createTraining(
                trainingAddDTO.getTrainingName(),
                trainingAddDTO.getTrainingDate(),
                trainingAddDTO.getTrainingDuration(),
                trainingAddDTO.getTrainerUsername(),
                trainingAddDTO.getTraineeUsername()
        );
        metrics.registerNewTraining();
        return new ResponseEntity<>("Training added successfully.", HttpStatus.CREATED);
    }

}
