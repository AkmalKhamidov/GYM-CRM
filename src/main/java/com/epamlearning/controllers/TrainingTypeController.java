package com.epamlearning.controllers;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.services.impl.TrainingTypeServiceImpl;
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
@RequestMapping("api/v1/training-type")
@Tag(name = "Training Type Controller", description = "Controller for managing training types")
public class TrainingTypeController implements BaseController{

    private final TrainingTypeServiceImpl trainingTypeServiceImpl;

    @Autowired
    public TrainingTypeController(TrainingTypeServiceImpl trainingTypeServiceImpl) {
        this.trainingTypeServiceImpl = trainingTypeServiceImpl;
    }

    @Operation(summary = "Get all training types", description = "Getting all training types")
    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAllTrainingTypes() {
        return new ResponseEntity<>(trainingTypeServiceImpl.findAll(), HttpStatus.OK);
    }

}
