package com.epamlearning.repositories;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>, BaseRepository {

//    @Query(value = "SELECT tr FROM Training tr WHERE ((?1 IS NULL AND ?2 IS NULL) OR (?1 IS NOT NULL AND ?2 IS NULL AND tr.trainingDate >= ?1) OR (?1 IS NULL AND ?2 IS NOT NULL AND tr.trainingDate <= ?2) OR (tr.trainingDate BETWEEN ?1 AND ?2)) AND (?3 IS NULL OR tr.trainingType = ?3) AND (?4 IS NOT NULL AND tr.trainer = ?4 OR ?4 IS NULL)")
//    @Query(name = "findTrainingsByTrainerAndCriteria") // error here
    @Query(value = "${findTrainingsByTrainerAndCriteria}", nativeQuery = true)
    List<Training> findTrainingsByTrainerAndCriteria(Trainer trainer, Date startDate, Date endDate, TrainingType trainingType);

    @Query(value = "SELECT tr FROM Training tr WHERE ((?1 IS NULL AND ?2 IS NULL) OR (?1 IS NOT NULL AND ?2 IS NULL AND tr.trainingDate >= ?1) OR (?1 IS NULL AND ?2 IS NOT NULL AND tr.trainingDate <= ?2) OR (tr.trainingDate BETWEEN ?1 AND ?2)) AND (?3 IS NULL OR tr.trainingType = ?3) AND (?4 IS NOT NULL AND tr.trainee = ?4 OR ?4 IS NULL)")
    List<Training> findTrainingsByTraineeAndCriteria(Trainee trainee, Date startDate, Date endDate, TrainingType trainingType);

    List<Training> findByTrainee(Trainee trainee);

    void deleteByTrainee(Trainee trainee);

    @Query(value = "SELECT t FROM Training t WHERE t.trainer = :trainer and t.trainee = :trainee and t != :training")
    boolean existsAnotherTrainingByTraineeAndTrainer(
            @Param("training") Training training,
            @Param("trainee") Trainee trainee,
            @Param("trainer") Trainer trainer);

//    @Query(name = "findTrainingsByTrainerAndTrainee")
    boolean existsTrainingByTraineeAndTrainer(Trainee trainee, Trainer trainer);
}
