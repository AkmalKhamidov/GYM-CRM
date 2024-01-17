package com.epamlearning.controllers;

import com.epamlearning.dtos.trainee.request.TraineeRegistrationRequestDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateRequestDTO;
import com.epamlearning.dtos.trainee.request.UpdateTrainersOfTraineeRequestDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.UserService;
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

    private final TraineeService traineeService;
    private final UserService userService;

    private final Mapper mapper;

    @Autowired
    public TraineeController(TraineeService traineeService, UserService userService, Mapper mapper) {
        this.traineeService = traineeService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Operation(summary = "Register trainee", description = "Registering trainee with default active (true)")
    @PostMapping("/register")
    public ResponseEntity<UserAuthDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationRequestDTO traineeDTO) {
        User user = userService.createUser(traineeDTO.getFirstName(), traineeDTO.getLastName());
        Trainee trainee = traineeService.createTrainee(user, traineeDTO.getAddress(), traineeDTO.getDateOfBirth());
        Trainee savedTrainee = traineeService.save(trainee);

        UserAuthDTO responseDTO = mapper.mapToDTO(savedTrainee, UserAuthDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get trainee profile", description = "Getting trainee profile by username")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@Parameter(description = "trainee username", example = "John.Wick") @PathVariable("username") String username) {
        Trainee trainee = traineeService.findByUsername(username);
        List<TrainerListResponseDTO> trainerListDTO =
                trainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        TraineeProfileResponseDTO responseDTO = mapper.mapToDTO(trainee, TraineeProfileResponseDTO.class);
        responseDTO.setTrainerList(trainerListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update trainee profile", description = "Updating trainee profile by username")
    @PutMapping("/update")
    public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(@Validated @RequestBody TraineeUpdateRequestDTO traineeDTO) {
        Trainee trainee = traineeService.findByUsername(traineeDTO.getUsername());
        trainee.setAddress(traineeDTO.getAddress());
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        User user = trainee.getUser();
        user.setFirstName(traineeDTO.getFirstName());
        user.setLastName(traineeDTO.getLastName());
        user.setActive(traineeDTO.isActive());
        trainee.setUser(user);
        Trainee savedTrainee = traineeService.save(trainee);
        List<TrainerListResponseDTO> trainerListDTO =
                savedTrainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        TraineeProfileResponseDTO responseDTO = mapper.mapToDTO(savedTrainee, TraineeProfileResponseDTO.class);
        responseDTO.setTrainerList(trainerListDTO);
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
        List<Trainer> trainers = trainersOfTraineeDTO.getTrainers().stream().map(trainer -> mapper.mapToModel(trainer, Trainer.class)).toList();
        Trainee updatedTrainee = traineeService.updateTrainersForTrainee(trainee.getId(), trainers);
        List<TrainerListResponseDTO> responseDTO =
                updatedTrainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
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
