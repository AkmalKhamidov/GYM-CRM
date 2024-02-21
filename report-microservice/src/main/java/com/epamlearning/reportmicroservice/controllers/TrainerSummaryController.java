package com.epamlearning.reportmicroservice.controllers;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.services.impl.TrainerSummaryServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monthly-summary")
public class TrainerSummaryController {
  private final TrainerSummaryServiceImpl trainerSummaryService;

  @Autowired
  public TrainerSummaryController(TrainerSummaryServiceImpl trainerSummaryService) {
    this.trainerSummaryService = trainerSummaryService;
  }

  @GetMapping
  public ResponseEntity<TrainerSummary> getByUsername(@RequestParam @Valid @NotNull @NotBlank String username){
    return new ResponseEntity<>(trainerSummaryService.calculate(username), HttpStatus.OK);
  }

}
