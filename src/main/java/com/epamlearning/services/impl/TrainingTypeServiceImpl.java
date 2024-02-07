package com.epamlearning.services.impl;

import com.epamlearning.dtos.trainingtype.response.TrainingTypeResponseDTO;
import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.enums.TrainingTypeName;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.mapper.TrainingTypeMapper;
import com.epamlearning.repositories.TrainingTypeRepository;
import com.epamlearning.services.TrainingTypeService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public TrainingType findById(Long id) {
        if(id == null) {
            log.warn("Training type ID is null.");
            throw new NotFoundException("Training type ID is null.");
        }
        Optional<TrainingType> trainingType = trainingTypeRepository.findById(id);
        if (trainingType.isEmpty()) {
            log.warn("TrainingType with ID: {} not found.", id);
            throw new NotFoundException("TrainingType with ID " + id + " not found.");
        }
        return trainingType.get();
    }

    @Override
    public List<TrainingTypeResponseDTO> findAll() {
        return trainingTypeMapper.trainingTypesToTrainingTypeResponseDTOs(trainingTypeRepository.findAll());
    }

    public TrainingType findByTrainingTypeName(TrainingTypeName trainingTypeName) {
        Optional<TrainingType> trainingType = Optional.ofNullable(trainingTypeRepository.findByTrainingTypeName(trainingTypeName));
        if (trainingType.isEmpty()) {
            log.warn("TrainingType with TrainingTypeName: {} not found.", trainingTypeName);
            return null;
        }
        return trainingType.get();
    }
}
