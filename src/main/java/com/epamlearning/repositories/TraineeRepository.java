package com.epamlearning.repositories;

import com.epamlearning.entities.Trainee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long>, BaseRepository {
    Optional<Trainee> findTraineeByUserUsername(String username);
}