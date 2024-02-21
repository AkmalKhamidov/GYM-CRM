package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;

import java.util.List;

public interface TrainerWorkloadService {

  void manageTrainerWorkload(TrainerWorkloadRequestDTO request);

  List<TrainerWorkload> getAllByUsername(String username);
}
