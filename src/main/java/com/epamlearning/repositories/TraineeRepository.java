package com.epamlearning.repositories;

import com.epamlearning.models.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long>, BaseRepository {
//    @Query(name = "findTraineeByUsername")
    Optional<Trainee> findTraineeByUserUsername(String username);
}