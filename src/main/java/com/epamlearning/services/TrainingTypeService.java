package com.epamlearning.services;

import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.enums.TrainingTypeName;
import com.epamlearning.repositories.TrainingTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainingTypeService implements BaseService<TrainingType> {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    // Only Get (Read/select) methods are required and implemented only.
    @Override
    public TrainingType findById(Long id) {
        if(id == null) {
            log.warn("ID is null.");
            throw new NullPointerException("ID is null.");
        }
        Optional<TrainingType> trainingType = trainingTypeRepository.findById(id);
        if (trainingType.isEmpty()) {
            log.warn("TrainingType with ID: {} not found.", id);
            throw new NotFoundException("TrainingType with ID " + id + " not found.");
        }
        return trainingType.get();
    }

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        return null;
    }

    @Override
    public TrainingType update(Long id, TrainingType trainingType) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }


    public TrainingType findByTrainingTypeName(TrainingTypeName trainingTypeName) {
        Optional<TrainingType> trainingType = Optional.ofNullable(trainingTypeRepository.findByTrainingTypeName(trainingTypeName));
        if (trainingType.isEmpty()) {
            log.warn("TrainingType with TrainingTypeName: {} not found.", trainingTypeName);
            return null;
        }
        return trainingType.get();
    }
    @Override
    public TrainingType findByUsername(String username) {
        return null;
    }
}
