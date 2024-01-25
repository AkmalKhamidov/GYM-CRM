package com.epamlearning.controllers;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.mapper.TrainingTypeMapper;
import com.epamlearning.services.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-type")
@Tag(name = "Training Type Controller", description = "Controller for managing training types")
public class TrainingTypeController implements BaseController{

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Operation(summary = "Get all training types", description = "Getting all training types")
    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAllTrainingTypes() {
        List<TrainingTypeResponseDTO> responseDTO = trainingTypeMapper.trainingTypesToTrainingTypeResponseDTOs(trainingTypeService.findAll());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
