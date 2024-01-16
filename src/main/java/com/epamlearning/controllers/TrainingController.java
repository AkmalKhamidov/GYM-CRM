package com.epamlearning.controllers;

import com.epamlearning.dtos.trainer.request.TrainerUsernameRequestDTO;
import com.epamlearning.dtos.training.request.TraineeTrainingsRequestDTO;
import com.epamlearning.dtos.training.response.TraineeTrainingsResponseDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.models.Training;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.TrainingTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController implements BaseController {

    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final Mapper mapper;

    @Autowired
    public TrainingController(TrainingService trainingService, TrainingTypeService trainingTypeService, Mapper mapper) {
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.mapper = mapper;
    }

    @GetMapping("/by-trainee")
    public ResponseEntity<List<TraineeTrainingsResponseDTO>> getTraineeTrainings(@Validated @RequestBody TraineeTrainingsRequestDTO trainingDTO) {
        List<Training> trainings = trainingService.findByTraineeAndCriteria(trainingDTO.getUsername(), trainingDTO.getDateFrom(), trainingDTO.getDateTo(), trainingTypeService.findByTrainingTypeName(trainingDTO.getTrainingTypeName()));
        List<TraineeTrainingsResponseDTO> responseDTO = mapper.mapToListDTO(trainings, TraineeTrainingsResponseDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}
