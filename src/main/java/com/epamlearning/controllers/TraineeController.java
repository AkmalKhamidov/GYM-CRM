package com.epamlearning.controllers;

import com.epamlearning.dtos.trainee.TraineeRegistrationDTO;
import com.epamlearning.dtos.user.response.UserRegistrationResponseDTO;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.User;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainee")
public class TraineeController implements BaseController {

    private final TraineeService traineeService;
    private final UserService userService;

    @Autowired
    public TraineeController(TraineeService traineeService, UserService userService) {
        this.traineeService = traineeService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody TraineeRegistrationDTO traineeDTO) {
        User user = userService.createUser(traineeDTO.getFirstName(), traineeDTO.getLastName());
        Trainee trainee = traineeService.createTrainee(user, traineeDTO.getAddress(), traineeDTO.getDateOfBirth());
        Trainee savedTrainee = traineeService.save(trainee);

        UserRegistrationResponseDTO responseDTO = new UserRegistrationResponseDTO(savedTrainee.getUser().getUsername(), savedTrainee.getUser().getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
