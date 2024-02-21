package com.epamlearning.reportmicroservice.repositories;

import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {

  boolean existsByUsername(String username);

  List<TrainerWorkload> findByUsername(String username);

  void deleteAllByTraineeUsername(String traineeUsername);
}
