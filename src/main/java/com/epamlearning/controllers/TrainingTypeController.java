package com.epamlearning.controllers;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.mapper.Mapper;
import com.epamlearning.services.TrainingTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-type")
public class TrainingTypeController implements BaseController{

    private final TrainingTypeService trainingTypeService;
    private final Mapper mapper;

    public TrainingTypeController(TrainingTypeService trainingTypeService, Mapper mapper) {
        this.trainingTypeService = trainingTypeService;
        this.mapper = mapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAllTrainingTypes() {
        List<TrainingTypeResponseDTO> responseDTO = trainingTypeService.findAll().stream().map(trainingType -> mapper.mapToDTO(trainingType, TrainingTypeResponseDTO.class)).toList();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
