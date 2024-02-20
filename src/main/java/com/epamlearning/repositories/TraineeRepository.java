package com.epamlearning.repositories;

import com.epamlearning.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long>, BaseRepository {
    Optional<Trainee> findTraineeByUserUsername(String username);
}