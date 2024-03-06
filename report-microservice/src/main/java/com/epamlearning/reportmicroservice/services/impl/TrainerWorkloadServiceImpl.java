package com.epamlearning.reportmicroservice.services.impl;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.mappers.TrainerWorkloadMapper;
import com.epamlearning.reportmicroservice.repositories.TrainerWorkloadRepository;
import com.epamlearning.reportmicroservice.services.TrainerWorkloadService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

  private final TrainerWorkloadMapper trainerWorkloadMapper;
  private final TrainerWorkloadRepository trainerWorkloadRepository;

  @Autowired
  public TrainerWorkloadServiceImpl(TrainerWorkloadMapper trainerWorkloadMapper,
      TrainerWorkloadRepository trainerWorkloadRepository) {
    this.trainerWorkloadMapper = trainerWorkloadMapper;
    this.trainerWorkloadRepository = trainerWorkloadRepository;
  }

  public List<TrainerWorkload> getAllTrainerWorkload() {
    return trainerWorkloadRepository.findAll();
  }

  @Override
  public List<TrainerWorkload> getAllByUsername(String username){
    return trainerWorkloadRepository.findByUsername(username);
  }

  @Override
  @Transactional
  public void manageTrainerWorkload(TrainerWorkloadRequestDTO request) {
      if(request.getActionType() == null)
        throw new NotFoundException("Action type cannot be null");
      switch (request.getActionType()) {
        case ADD -> addTrainerWorkload(request);
        case DELETE -> deleteTrainerWorkload(request);
        default -> throw new NotFoundException("Unsupported action type");
      }
  }

  public void addTrainerWorkload(TrainerWorkloadRequestDTO request) {
    trainerWorkloadRepository.save(trainerWorkloadMapper.TrainerWorkloadRequestDTOToTrainerWorkload(request));
  }

  public void deleteTrainerWorkload(TrainerWorkloadRequestDTO request) {
    TrainerWorkload trainerWorkload = trainerWorkloadMapper.TrainerWorkloadRequestDTOToTrainerWorkload(request);
    trainerWorkloadRepository.deleteAllByTraineeUsername(trainerWorkload.getTraineeUsername());
  }

}
