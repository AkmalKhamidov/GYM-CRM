package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.services.impl.TrainingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${server.servlet.context-path}/training")
@Tag(name = "Training Controller", description = "Controller for managing trainings")
public class TrainingController implements BaseController {

    private final TrainingServiceImpl trainingServiceImpl;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TrainingController(TrainingServiceImpl trainingServiceImpl, UserEngagementMetrics metrics) {
        this.trainingServiceImpl = trainingServiceImpl;
        this.metrics = metrics;
    }


    @PreAuthorize("hasAnyRole('ROLE_TRAINEE', 'ROLE_TRAINER') and " +
            "((hasRole('ROLE_TRAINEE') and not hasRole('ROLE_TRAINER') and " +
            "#trainingAddDTO.traineeUsername == authentication.principal.username) or " +
            "(hasRole('ROLE_TRAINER') and not hasRole('ROLE_TRAINEE') and " +
            "#trainingAddDTO.trainerUsername == authentication.principal.username)) and " +
            "authentication.principal.enabled == true")
    @Operation(summary = "Add training", description = "Adding training with trainer training type")
    @PostMapping
    public ResponseEntity<Void> addTraining(@Validated @RequestBody TrainingAddRequestDTO trainingAddDTO) {
        trainingServiceImpl.createTraining(
                trainingAddDTO.getTrainingName(),
                trainingAddDTO.getTrainingDate(),
                trainingAddDTO.getTrainingDuration(),
                trainingAddDTO.getTrainerUsername(),
                trainingAddDTO.getTraineeUsername()
        );
        metrics.registerNewTraining();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
