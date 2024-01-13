package com.epamlearning.controllers;

import com.epamlearning.dtos.trainee.request.TraineeRegistrationDTO;
import com.epamlearning.dtos.trainee.response.TraineeProfileDTO;
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

        UserAuthDTO responseDTO = mapper.mapToDTO(savedTrainee.getUser(), UserAuthDTO.class); //new UserAuthDTO(savedTrainee.getUser().getUsername(), savedTrainee.getUser().getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<TraineeProfileDTO> getTraineeProfile(@PathVariable String username) {
//        Trainee trainee = traineeService.findByUsername(username);
//        TraineeProfileDTO responseDTO = mapper.mapToDTO(trainee, TraineeProfileDTO.class);
//        return new ResponseEntity<>(traineeService.findByUsername(username), HttpStatus.OK);
//    }

}
