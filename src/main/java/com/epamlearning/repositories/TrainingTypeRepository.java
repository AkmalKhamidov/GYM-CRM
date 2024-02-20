package com.epamlearning.repositories;

import com.epamlearning.entities.TrainingType;
import com.epamlearning.entities.enums.TrainingTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long>, BaseRepository {

    TrainingType findByTrainingTypeName(TrainingTypeName trainingTypeName);

}
