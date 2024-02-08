package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.trainer.request.TrainerRegistrationRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerRegistrationResponseDTO;
import com.epamlearning.dtos.training.request.TrainerTrainingsRequestDTO;
import com.epamlearning.dtos.training.response.TrainerTrainingsResponseDTO;
import com.epamlearning.services.impl.TrainerServiceImpl;
import com.epamlearning.services.impl.TrainingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/trainer")
@Tag(name = "Trainer Controller", description = "Controller for managing trainers")
public class TrainerController implements BaseController {

    private final TrainerServiceImpl trainerServiceImpl;
    private final TrainingServiceImpl trainingService;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TrainerController(TrainerServiceImpl trainerServiceImpl, TrainingServiceImpl trainingService, UserEngagementMetrics metrics) {
        this.trainerServiceImpl = trainerServiceImpl;
        this.trainingService = trainingService;
        this.metrics = metrics;
    }

    @Operation(summary = "Register trainer", description = "Registering trainer with default active (true)")
    @PostMapping("/register")
    public ResponseEntity<TrainerRegistrationResponseDTO> registerTrainer(@RequestBody TrainerRegistrationRequestDTO trainerDTO) {
        TrainerRegistrationResponseDTO responseDTO = trainerServiceImpl.createTrainer(
                trainerDTO.getFirstName(),
                trainerDTO.getLastName(),
                trainerDTO.getSpecializationId());
        metrics.registerNewTrainer();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Get trainer profile", description = "Getting trainer profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> getTrainerProfile(@Parameter(description = "trainer username", example = "John.Smith")
                                                                       @PathVariable("username") String username) {
        return new ResponseEntity<>(trainerServiceImpl.findByUsername(username), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Update trainer profile", description = "Updating trainer profile by username")
    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> updateTraineeProfile(@Parameter(description = "trainer username", example = "John.Smith")
                                                                              @PathVariable("username")
                                                                              @NotNull(message = "Username cannot be null")
                                                                              @NotBlank(message = "Username cannot be blank") String username,
                                                                          @Validated @RequestBody TrainerUpdateRequestDTO trainerDTO) {
        return new ResponseEntity<>(trainerServiceImpl.update(username, trainerDTO), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Update trainer active", description = "Update trainer active (status)")
    @PatchMapping("/{username}/{active}")
    public ResponseEntity<Void> updateTraineeActive(@Parameter(description = "Trainer username", example = "John.Smith")
                                                      @PathVariable("username") String username,
                                                      @Parameter(description = "Trainer active (status) (true/false)", example = "true")
                                                      @PathVariable("active") boolean isActive) {
        trainerServiceImpl.updateActive(username, isActive);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.principal.username and principal.enabled == true")
    @Operation(summary = "Get trainer trainings", description = "Getting trainer trainings by username and optional criteria")
    @GetMapping("{username}/trainings")
    public ResponseEntity<List<TrainerTrainingsResponseDTO>> getTrainerTrainings(@Parameter(description = "trainer username", example = "John.Smith")
                                                                                     @PathVariable("username")
                                                                                     @NotNull(message = "Username cannot be null")
                                                                                     @NotBlank(message = "Username cannot be blank")
                                                                                     String username,
                                                                                 @Validated TrainerTrainingsRequestDTO trainingDTO) {
        List<TrainerTrainingsResponseDTO> responseDTO =
                trainingService.findByTrainerAndCriteria(
                        username,
                        trainingDTO.getDateFrom(),
                        trainingDTO.getDateTo(),
                        null,
                        StringUtils.trimToNull(trainingDTO.getTraineeName())
                );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
