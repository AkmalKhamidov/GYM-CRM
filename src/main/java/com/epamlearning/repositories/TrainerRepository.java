package com.epamlearning.repositories;

import com.epamlearning.entities.Trainee;
import com.epamlearning.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long>, BaseRepository{
    Optional<Trainer> findTrainerByUserUsername(String username);

    @Query(value = "SELECT t FROM Trainer t WHERE t.user.isActive = true AND t NOT IN (SELECT tr.trainers FROM Trainee tr WHERE tr = ?1)")
    List<Trainer> findNotAssignedActiveTrainersOfTrainee(Trainee trainee);

}
