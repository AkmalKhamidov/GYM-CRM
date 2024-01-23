package com.epamlearning.controllers;

import com.epamlearning.actuator.metrics.UserEngagementMetrics;
import com.epamlearning.dtos.trainee.request.TraineeRegistrationRequestDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import com.epamlearning.mapper.TraineeMapper;
import com.epamlearning.mapper.TrainerMapper;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
@Tag(name = "Trainee controller", description = "Controller for managing trainees")
public class TraineeController implements BaseController {

    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserEngagementMetrics metrics;

    @Autowired
    public TraineeController(TraineeMapper traineeMapper, TrainerMapper trainerMapper, TraineeService traineeService,
                             TrainerService trainerService, UserEngagementMetrics metrics) {
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.metrics = metrics;
    }

    @Operation(summary = "Register trainee", description = "Registering trainee with default active (true)")
    @PostMapping("/register")
    public ResponseEntity<UserAuthDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationRequestDTO traineeDTO) {
        Trainee trainee = traineeService.createTrainee(traineeDTO.getFirstName(), traineeDTO.getLastName(), traineeDTO.getAddress(), traineeDTO.getDateOfBirth());
        Trainee savedTrainee = traineeService.save(trainee);

        metrics.registerNewTrainee();

        UserAuthDTO responseDTO = traineeMapper.traineeToUserAuthDTO(savedTrainee);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get trainee profile", description = "Getting trainee profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@Parameter(description = "trainee username", example = "John.Wick")
                                                                           @PathVariable("username") String username) {
        Trainee trainee = traineeService.findByUsername(username);
        TraineeProfileResponseDTO responseDTO = traineeMapper.traineeToTraineeProfileResponseToDTO(trainee);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update trainee profile", description = "Updating trainee profile by username")
    @PutMapping("/update")
    public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(@Validated @RequestBody TraineeUpdateRequestDTO traineeDTO) {
        Trainee trainee = traineeService.findByUsername(traineeDTO.getUsername());
        Trainee savedTrainee = traineeService.update(trainee.getId(), traineeMapper.traineeUpdateRequestDTOToTrainee(traineeDTO));
        TraineeProfileResponseDTO responseDTO = traineeMapper.traineeToTraineeProfileResponseToDTO(savedTrainee);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete trainee", description = "Deleting trainee by username")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteTrainee(@Parameter(description = "trainee username", example = "John.Wick") @PathVariable("username") String username) {
        traineeService.deleteByUsername(username);
        return new ResponseEntity<>("Trainee with username: " + username + " was deleted.", HttpStatus.OK);
    }

    @Operation(summary = "Update trainers of trainee", description = "Updating trainers of trainee by username")
    @PutMapping("/update/trainers")
    public ResponseEntity<List<TrainerListResponseDTO>> updateTrainersOfTrainee(@Validated @RequestBody UpdateTrainersOfTraineeRequestDTO trainersOfTraineeDTO) {
        Trainee trainee = traineeService.findByUsername(trainersOfTraineeDTO.getUsername());
        List<Trainer> trainers = trainersOfTraineeDTO.getTrainers().stream().map(trainer -> trainerService.findByUsername(trainer.getUsername())).toList();
        Trainee updatedTrainee = traineeService.updateTrainersForTrainee(trainee.getId(), trainers);
        List<TrainerListResponseDTO> responseDTO = trainerMapper.trainersToTrainerListResponseDTOs(updatedTrainee.getTrainers());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update trainee active", description = "Updating trainee active by username")
    @PatchMapping("/updateActive/{username}/{active}")
    public ResponseEntity<String> updateTraineeActive(@Parameter(description = "trainee username", example = "John.Wick") @PathVariable("username") String username,
                                                      @Parameter(description = "trainee isActive (true/false)", example = "true") @PathVariable("active") boolean isActive) {
        String resultText = isActive ? "activated" : "deactivated";
        Trainee updatedTrainee = traineeService.updateActive(traineeService.findByUsername(username).getId(), isActive);
        return new ResponseEntity<>("Trainee with username: " + username + " was " + resultText + ".", HttpStatus.OK);
    }
}
