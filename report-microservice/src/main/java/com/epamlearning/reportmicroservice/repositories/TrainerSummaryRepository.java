package com.epamlearning.reportmicroservice.repositories;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, String>{

    Optional<TrainerSummary> findByUsername(String username);

    List<TrainerSummary> findByFirstNameAndLastName(String firstName, String lastName);

}
