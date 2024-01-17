package com.epamlearning.repositories;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>, BaseRepository {

    @Query(value = "SELECT tr FROM Training tr WHERE tr.trainer = ?1 " +
            "AND (tr.trainingDate >= COALESCE(CAST(?2 AS date), tr.trainingDate)) " +
            "AND (tr.trainingDate <= COALESCE(CAST(?3 AS date), tr.trainingDate)) " +
            "AND (?4 IS NULL OR tr.trainingType = ?4) " +
            "AND (?5 IS NULL OR (tr.trainee.user.firstName = ?5 OR tr.trainee.user.lastName = ?5))")
    List<Training> findTrainingsByTrainerAndCriteria(Trainer trainer, Date startDate, Date endDate, TrainingType trainingType, String traineeName);

    @Query(value = "SELECT tr FROM Training tr WHERE tr.trainee = ?1 " +
            "AND (tr.trainingDate >= COALESCE(CAST(?2 AS date), tr.trainingDate)) " +
            "AND (tr.trainingDate <= COALESCE(CAST(?3 AS date), tr.trainingDate)) " +
            "AND (?4 IS NULL OR tr.trainingType = ?4) " +
            "AND (?5 IS NULL OR (tr.trainer.user.firstName = ?5 OR tr.trainer.user.lastName = ?5))")
    List<Training> findTrainingsByTraineeAndCriteria(Trainee trainee, Date startDate, Date endDate, TrainingType trainingType, String trainerName);


    List<Training> findByTrainee(Trainee trainee);

    void deleteByTrainee(Trainee trainee);

    @Query(value = "SELECT t FROM Training t WHERE t.trainer = ?3 and t.trainee = ?2 and t != ?1")
    boolean existsAnotherTrainingByTraineeAndTrainer(
            Training training,
            Trainee trainee,
            Trainer trainer);

    boolean existsTrainingByTraineeAndTrainer(Trainee trainee, Trainer trainer);
}
