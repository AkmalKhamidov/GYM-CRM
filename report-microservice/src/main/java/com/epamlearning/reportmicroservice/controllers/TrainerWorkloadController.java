package com.epamlearning.reportmicroservice.controllers;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.services.impl.TrainerWorkloadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trainer-workload")
public class TrainerWorkloadController {

  private final TrainerWorkloadServiceImpl trainerWorkloadService;

  @Autowired
  public TrainerWorkloadController(TrainerWorkloadServiceImpl trainerWorkloadService) {
    this.trainerWorkloadService = trainerWorkloadService;
  }

  @PostMapping
  public ResponseEntity<Void> manageTrainerWorkload(@RequestBody
                                                      @Validated TrainerWorkloadRequestDTO request) {
    trainerWorkloadService.manageTrainerWorkload(request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
