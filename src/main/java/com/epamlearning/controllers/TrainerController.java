package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.trainer.request.TrainerRegistrationRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.entities.Trainer;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
@Tag(name = "Trainer Controller", description = "Controller for managing trainers")
public class TrainerController implements BaseController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TrainerController(TrainerService trainerService, TrainerMapper trainerMapper, UserEngagementMetrics metrics) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
        this.metrics = metrics;
    }

    @Operation(summary = "Register trainer", description = "Registering trainer with default active (true)")
    @PostMapping("/register")
    public ResponseEntity<UserAuthDTO> registerTrainer(@RequestBody TrainerRegistrationRequestDTO trainerDTO) {
        Trainer trainer = trainerService.createTrainer(trainerDTO.getFirstName(),
                trainerDTO.getLastName(), trainerDTO.getSpecializationId());
        Trainer savedTrainer = trainerService.save(trainer);

        metrics.registerNewTrainer();

        UserAuthDTO responseDTO = trainerMapper.trainerToUserAuthDTO(savedTrainer);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get trainer profile", description = "Getting trainer profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> getTrainerProfile(@Parameter(description = "trainer username", example = "John.Smith")
                                                                       @PathVariable("username") String username) {
        Trainer trainer = trainerService.findByUsername(username);
        TrainerProfileResponseDTO responseDTO = trainerMapper.trainerToTrainerProfileResponseDTO(trainer);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update trainer profile", description = "Updating trainer profile by username")
    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> updateTraineeProfile(@Parameter(description = "trainer username", example = "John.Smith")
                                                                              @PathVariable("username")
                                                                              @NotNull(message = "Username cannot be null")
                                                                              @NotBlank(message = "Username cannot be blank") String username,
                                                                          @Validated @RequestBody TrainerUpdateRequestDTO trainerDTO) {
        Trainer trainer = trainerService.findByUsername(username);
        Trainer savedTrainer = trainerService.update(trainer.getId(), trainerMapper.trainerUpdateRequestDTOToTrainer(trainerDTO));
        TrainerProfileResponseDTO responseDTO = trainerMapper.trainerToTrainerProfileResponseDTO(savedTrainer);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all trainers", description = "Getting all active trainers not assigned on trainee (trainee username)")
    @GetMapping("/not-assigned-on-trainee/{username}")
    public ResponseEntity<List<TrainerListResponseDTO>> getNotAssignedTrainers(@Parameter(description = "trainee username", example = "John.Wick")
                                                                               @PathVariable("username") String username) {
        List<Trainer> trainers = trainerService.findNotAssignedActiveTrainers(username);
        List<TrainerListResponseDTO> responseDTO = trainerMapper.trainersToTrainerListResponseDTOs(trainers);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update trainer active", description = "Update trainer active (status)")
    @PatchMapping("/{username}/{active}")
    public ResponseEntity<String> updateTraineeActive(@Parameter(description = "Trainer username", example = "John.Smith")
                                                      @PathVariable("username") String username,
                                                      @Parameter(description = "Trainer active (status) (true/false)", example = "true")
                                                      @PathVariable("active") boolean isActive) {
        String resultText = isActive ? "activated" : "deactivated";
        Trainer updatedTrainer = trainerService.updateActive(trainerService.findByUsername(username).getId(), isActive);
        return new ResponseEntity<>("Trainer with username: " + username + " was " + resultText + ".", HttpStatus.OK);
    }

}
