package com.epamlearning.controllers;

import com.epamlearning.dtos.trainee.request.TraineeRegistrationDTO;
import com.epamlearning.dtos.trainee.request.TraineeUpdateDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.models.Trainee;
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
    public ResponseEntity<UserAuthDTO> registerTrainee(@Validated @RequestBody TraineeRegistrationDTO traineeDTO) {
        User user = userService.createUser(traineeDTO.firstName(), traineeDTO.lastName());
        Trainee trainee = traineeService.createTrainee(user, traineeDTO.address(), traineeDTO.dateOfBirth());
        Trainee savedTrainee = traineeService.save(trainee);

        UserAuthDTO responseDTO = mapper.mapToDTO(savedTrainee, UserAuthDTO.class); //new UserAuthDTO(savedTrainee.getUser().getUsername(), savedTrainee.getUser().getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDTO> getTraineeProfile(@PathVariable("username") String username) {
        Trainee trainee = traineeService.findByUsername(username);
        List<TrainerListResponseDTO> trainerListDTO =
                trainee.getTrainers().stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        TraineeProfileDTO responseDTO = mapper.mapToDTO(trainee, TraineeProfileDTO.class);
        responseDTO.setTrainerList(trainerListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TraineeProfileDTO> updateTraineeProfile(@Validated @RequestBody TraineeUpdateDTO traineeDTO) {
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
        TraineeProfileDTO responseDTO = mapper.mapToDTO(savedTrainee, TraineeProfileDTO.class);
        responseDTO.setTrainerList(trainerListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteByUsername(username);
        return new ResponseEntity<>("Trainee with username: " + username + " was deleted.", HttpStatus.OK);
    }



}
