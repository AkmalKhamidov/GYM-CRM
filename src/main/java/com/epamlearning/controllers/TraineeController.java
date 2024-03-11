package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.trainee.request.TraineeRegistrationRequestDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainee.response.TraineeRegistrationResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.training.request.TraineeTrainingsRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.microservices.report.ReportServiceClient;
import com.epamlearning.microservices.report.dtos.TrainerWorkloadDTO;
import com.epamlearning.services.impl.TraineeServiceImpl;
import com.epamlearning.services.impl.TrainerServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import com.epamlearning.services.impl.TrainingTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
@Tag(name = "Trainee controller", description = "Controller for managing trainees")
public class TraineeController implements BaseController {

    private final TraineeServiceImpl traineeService;
    private final TrainingServiceImpl trainingService;
    private final TrainingTypeServiceImpl trainingTypeService;
    private final TrainerServiceImpl trainerService;
    private final UserEngagementMetrics metrics;

    private final ReportServiceClient workloadClient;

    @Autowired
    public TraineeController(TraineeServiceImpl traineeService, TrainingServiceImpl trainingService, TrainingTypeServiceImpl trainingTypeService, TrainerServiceImpl trainerService, UserEngagementMetrics metrics,
        ReportServiceClient workloadClient) {
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.metrics = metrics;
      this.workloadClient = workloadClient;
    }

    @Operation(summary = "Register trainee", description = "Registering trainee with default active (true)")
    @PostMapping("/register")
    public ResponseEntity<TraineeRegistrationResponseDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationRequestDTO traineeDTO) {
        TraineeRegistrationResponseDTO responseDTO =  traineeService.createTrainee(
                traineeDTO.getFirstName(),
                traineeDTO.getLastName(),
                traineeDTO.getAddress(),
                traineeDTO.getDateOfBirth());
        metrics.registerNewTrainee();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Get trainee profile", description = "Getting trainee profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@Parameter(description = "trainee username", example = "John.Wick")
                                                                           @PathVariable("username") String username) {
        return new ResponseEntity<>(traineeService.findByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Update trainee profile", description = "Updating trainee profile by username")
    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(@PathVariable("username")
                                                                            @Parameter(description = "Trainee username", example = "John.Wick")
                                                                              @NotNull(message = "Username cannot be null")
                                                                              @NotBlank(message = "Username cannot be blank") String username,
                                                                          @Validated @RequestBody TraineeUpdateRequestDTO traineeDTO) {
        return new ResponseEntity<>(traineeService.update(username, traineeDTO), HttpStatus.OK);
    }
    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Get all not assigned trainers on trainee", description = "Getting all active trainers not assigned on trainee (trainee username)")
    @GetMapping("/{username}/not-assigned-trainers")
    public ResponseEntity<List<TrainerListResponseDTO>> getNotAssignedTrainers(@Parameter(description = "trainee username", example = "John.Wick")
                                                                               @PathVariable("username") String username) {
        return new ResponseEntity<>(trainerService.findNotAssignedActiveTrainers(username), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username")
    @Operation(summary = "Delete trainee", description = "Deleting trainee by username")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(@Parameter(description = "trainee username", example = "John.Wick")
                                                    @PathVariable("username")
                                                    @NotNull(message = "Username cannot be null")
                                                    @NotBlank(message = "Username cannot be blank")
                                                    String username) {
        traineeService.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Update trainers of trainee", description = "Updating trainers of trainee by username")
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerListResponseDTO>> updateTrainersOfTrainee(@Parameter(description = "trainee username", example = "John.Wick")
                                                                                @PathVariable("username")
                                                                                @NotNull(message = "Username cannot be null")
                                                                                @NotBlank(message = "Username cannot be blank")
                                                                                String username,
                                                                                @Validated @RequestBody UpdateTrainersOfTraineeRequestDTO trainersOfTraineeDTO) {
        return new ResponseEntity<>(traineeService.updateTrainersForTrainee(username, trainersOfTraineeDTO), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username")
    @Operation(summary = "Update trainee active", description = "Updating trainee active by username")
    @PatchMapping("/{username}/{active}")
    public ResponseEntity<Void> updateTraineeActive(@Parameter(description = "trainee username", example = "John.Wick")
                                                          @PathVariable("username")
                                                          @NotNull(message = "Username cannot be null")
                                                          @NotBlank(message = "Username cannot be blank")
                                                          String username,
                                                      @Parameter(description = "trainee isActive (true/false)", example = "true")
                                                      @PathVariable("active")
                                                      boolean isActive) {
        String resultText = isActive ? "activated" : "deactivated";
        traineeService.updateActive(username, isActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Get trainee trainings", description = "Getting trainee trainings by username and optional criteria")
    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TraineeTrainingsResponseDTO>> getTraineeTrainings(@Parameter(description = "trainee username", example = "John.Wick")
                                                                                     @PathVariable("username")
                                                                                     @NotNull(message = "Username cannot be null")
                                                                                     @NotBlank(message = "Username cannot be blank")
                                                                                     String username,
                                                                                 @Validated TraineeTrainingsRequestDTO trainingDTO) {
        List<TraineeTrainingsResponseDTO> responseDTO =
                trainingService.findByTraineeAndCriteria(
                        username,
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        trainingTypeService.findByTrainingTypeName(trainingDTO.getTrainingTypeName()),
                        StringUtils.trimToNull(trainingDTO.getTrainerName())
                );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
