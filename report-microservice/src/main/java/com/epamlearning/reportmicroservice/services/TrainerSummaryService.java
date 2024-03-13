package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.entities.enums.ActionType;

import java.util.List;

public interface TrainerSummaryService {
  List<TrainerSummary> findByFirstNameAndLastName(String firstName, String lastName);
  TrainerSummary findByUsername(String username);
  TrainerSummary update(TrainerWorkloadRequestDTO trainerWorkload);
}
