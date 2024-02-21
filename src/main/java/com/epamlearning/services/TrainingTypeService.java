package com.epamlearning.services;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.enums.TrainingTypeName;

import java.util.List;

public interface TrainingTypeService extends BaseService{
    TrainingType findById(Long id);
    List<TrainingTypeResponseDTO> findAll();
    TrainingType findByTrainingTypeName(TrainingTypeName trainingTypeName);
}
