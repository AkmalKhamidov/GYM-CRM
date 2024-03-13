package com.epamlearning.reportmicroservice.controllers;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.services.impl.TrainerSummaryServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monthly-summary")
public class TrainerSummaryController {
    private final TrainerSummaryServiceImpl trainerSummaryService;

    @Autowired
    public TrainerSummaryController(TrainerSummaryServiceImpl trainerSummaryService) {
        this.trainerSummaryService = trainerSummaryService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerSummary> getByUsername(@PathVariable("username") @Valid @NotNull @NotBlank String username) {
        return new ResponseEntity<>(trainerSummaryService.findByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainerSummary>> getByFirstNameAndLastName(@RequestParam("firstName") @Valid @NotNull @NotBlank String firstName,
                                                                          @RequestParam("lastName") @Valid @NotNull @NotBlank String lastName) {
        return new ResponseEntity<>(trainerSummaryService.findByFirstNameAndLastName(firstName, lastName), HttpStatus.OK);
    }

}
