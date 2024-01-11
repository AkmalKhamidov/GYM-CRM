package com.epamlearning.controllers;

import com.epamlearning.dtos.trainer.TrainerRegistrationDTO;
import com.epamlearning.dtos.user.response.UserRegistrationResponseDTO;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.UserService;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
public class TrainerController implements BaseController {

    private final TrainerService trainerService;
    private final UserService userService;

    @Autowired
    public TrainerController(TrainerService trainerService, UserService userService) {
        this.trainerService = trainerService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody TrainerRegistrationDTO trainerDTO) {
        User user = userService.createUser(trainerDTO.getFirstName(), trainerDTO.getLastName());
        Trainer trainer = trainerService.createTrainer(user, trainerDTO.getSpecializationId());
        Trainer savedTrainer = trainerService.save(trainer);

        UserRegistrationResponseDTO responseDTO = new UserRegistrationResponseDTO(savedTrainer.getUser().getUsername(), savedTrainer.getUser().getPassword());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

}
