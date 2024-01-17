package com.epamlearning.controllers;

import com.epamlearning.dtos.trainee.response.TraineeListResponseDTO;
import com.epamlearning.dtos.trainer.request.TrainerRegistrationRequestDTO;
import com.epamlearning.dtos.trainer.request.TrainerUpdateRequestDTO;
import com.epamlearning.dtos.trainer.response.TrainerListResponseDTO;
import com.epamlearning.dtos.trainer.response.TrainerProfileResponseDTO;
import com.epamlearning.dtos.user.UserAuthDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.User;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController implements BaseController {

    private final TrainerService trainerService;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public TrainerController(TrainerService trainerService, UserService userService, Mapper mapper) {
        this.trainerService = trainerService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuthDTO> registerTrainer(@RequestBody TrainerRegistrationRequestDTO trainerDTO) {
        User user = userService.createUser(trainerDTO.getFirstName(), trainerDTO.getLastName());
        Trainer trainer = trainerService.createTrainer(user, trainerDTO.getSpecializationId());
        Trainer savedTrainer = trainerService.save(trainer);

        UserAuthDTO responseDTO = mapper.mapToDTO(savedTrainer, UserAuthDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> getTrainerProfile(@PathVariable("username") String username) {
        Trainer trainer = trainerService.findByUsername(username);
        List<TraineeListResponseDTO> traineeListDTO =
                trainer.getTrainees().stream().map(trainee -> mapper.mapToDTO(trainee, TraineeListResponseDTO.class)).toList();
        TrainerProfileResponseDTO responseDTO = mapper.mapToDTO(trainer, TrainerProfileResponseDTO.class);
        responseDTO.setTraineeList(traineeListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TrainerProfileResponseDTO> updateTraineeProfile(@Validated @RequestBody TrainerUpdateRequestDTO trainerDTO) {
        Trainer trainer = trainerService.findByUsername(trainerDTO.getUsername());
        User user = trainer.getUser();
        user.setFirstName(trainerDTO.getFirstName());
        user.setLastName(trainerDTO.getLastName());
        user.setActive(trainerDTO.isActive());
        trainer.setUser(user);
        Trainer savedTrainer = trainerService.save(trainer);
        List<TraineeListResponseDTO> traineeListDTO =
                savedTrainer.getTrainees().stream().map(trainee -> mapper.mapToDTO(trainee, TraineeListResponseDTO.class)).toList();
        TrainerProfileResponseDTO responseDTO = mapper.mapToDTO(savedTrainer, TrainerProfileResponseDTO.class);
        responseDTO.setTraineeList(traineeListDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/not-assigned-on-trainee/{username}")
    public ResponseEntity<List<TrainerListResponseDTO>> getNotAssignedTrainers(@PathVariable("username") String username) {
        List<Trainer> trainers = trainerService.findNotAssignedActiveTrainers(username);
        List<TrainerListResponseDTO> responseDTO = trainers.stream().map(trainer -> mapper.mapToDTO(trainer, TrainerListResponseDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PatchMapping("/updateActive/{username}/{active}")
    public ResponseEntity<String> updateTraineeActive(@PathVariable("username") String username, @PathVariable("active") boolean isActive) {
        String resultText = isActive ? "activated" : "deactivated";
        Trainer updatedTrainer = trainerService.updateActive(trainerService.findByUsername(username).getId(), isActive);
        return new ResponseEntity<>("Trainer with username: " + username + " was " + resultText + ".", HttpStatus.OK);
    }

}
