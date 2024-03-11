package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.training.request.TrainingAddRequestDTO;
import com.epamlearning.entities.Training;
import com.epamlearning.microservices.report.ReportServiceClient;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import com.epamlearning.producer.MessageProducer;
import com.epamlearning.services.impl.TrainingServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;


@RestController
@RequestMapping("/training")
@Tag(name = "Training Controller", description = "Controller for managing trainings")
@Slf4j
public class TrainingController implements BaseController {

    private final TrainingServiceImpl trainingServiceImpl;
    private final UserEngagementMetrics metrics;
    private final ReportServiceClient workloadClient;
    private final MessageProducer producer;

    @Autowired
    public TrainingController(TrainingServiceImpl trainingServiceImpl, UserEngagementMetrics metrics,
                              @Qualifier("com.epamlearning.microservices.report.ReportServiceClient") ReportServiceClient workloadClient, MessageProducer producer) {
        this.trainingServiceImpl = trainingServiceImpl;
        this.metrics = metrics;
        this.workloadClient = workloadClient;
        this.producer = producer;
    }


    @PreAuthorize("hasAnyRole('ROLE_TRAINEE', 'ROLE_TRAINER') and " +
            "((hasRole('ROLE_TRAINEE') and not hasRole('ROLE_TRAINER') and " +
            "#trainingAddDTO.traineeUsername == authentication.principal.username) or " +
            "(hasRole('ROLE_TRAINER') and not hasRole('ROLE_TRAINEE') and " +
            "#trainingAddDTO.trainerUsername == authentication.principal.username)) and " +
            "authentication.principal.enabled == true")
    @Operation(summary = "Add training", description = "Adding training with trainer training type")
    @CircuitBreaker(name = "cb-manage-trainer-workload", fallbackMethod = "fallbackAddTraining")
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

    private ResponseEntity<Void> fallbackAddTraining(TrainingAddRequestDTO trainingAddDTO, Throwable e) {
        log.info("AddTrainingFallback: Report Service is not available");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
