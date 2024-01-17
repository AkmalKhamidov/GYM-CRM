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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainee")
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

    @PostMapping("/register")
    public ResponseEntity<UserAuthDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationRequestDTO traineeDTO) {
        User user = userService.createUser(traineeDTO.getFirstName(), traineeDTO.getLastName());
        Trainee trainee = traineeService.createTrainee(user, traineeDTO.getAddress(), traineeDTO.getDateOfBirth());
        Trainee savedTrainee = traineeService.save(trainee);

        UserAuthDTO responseDTO = mapper.mapToDTO(savedTrainee, UserAuthDTO.class); //new UserAuthDTO(savedTrainee.getUser().getUsername(), savedTrainee.getUser().getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@PathVariable("username") String username) {
        Trainee trainee = traineeService.findByUsername(username);
        List<TrainerListResponseDTO> trainerListDTO =
                trainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        TraineeProfileResponseDTO responseDTO = mapper.mapToDTO(trainee, TraineeProfileResponseDTO.class);
        responseDTO.setTrainerList(trainerListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

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

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteByUsername(username);
        return new ResponseEntity<>("Trainee with username: " + username + " was deleted.", HttpStatus.OK);
    }

    @PutMapping("/update/trainers")
    public ResponseEntity<List<TrainerListResponseDTO>> updateTrainersOfTrainee(@Validated @RequestBody UpdateTrainersOfTraineeRequestDTO trainersOfTraineeDTO) {
        Trainee trainee = traineeService.findByUsername(trainersOfTraineeDTO.getUsername());
        List<Trainer> trainers = trainersOfTraineeDTO.getTrainers().stream().map(trainer -> mapper.mapToModel(trainer, Trainer.class)).toList();
        Trainee updatedTrainee = traineeService.updateTrainersForTrainee(trainee.getId(), trainers);
        List<TrainerListResponseDTO> responseDTO =
                updatedTrainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/updateActive/{username}/{active}")
    public ResponseEntity<String> updateTraineeActive(@PathVariable("username") String username, @PathVariable("active") boolean isActive) {
        String resultText = isActive ? "activated" : "deactivated";
        Trainee updatedTrainee = traineeService.updateActive(traineeService.findByUsername(username).getId(), isActive);
        return new ResponseEntity<>("Trainee with username: " + username + " was " + resultText + ".", HttpStatus.OK);
    }
}
