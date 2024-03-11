package com.epamlearning.reportmicroservice.services.impl;

import com.epamlearning.reportmicroservice.dtos.TrainerWorkloadRequestDTO;
import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.enums.ActionType;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.repositories.TrainerSummaryRepository;
import com.epamlearning.reportmicroservice.services.TrainerSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
@Slf4j
public class TrainerSummaryServiceImpl implements TrainerSummaryService {

    private final TrainerSummaryRepository trainerSummaryRepository;

    @Autowired
    public TrainerSummaryServiceImpl(TrainerSummaryRepository trainerSummaryRepository) {
        this.trainerSummaryRepository = trainerSummaryRepository;
    }

    @Override
    public List<TrainerSummary> findByFirstNameAndLastName(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank())
            throw new NotFoundException("First name and last name cannot be null");
        List<TrainerSummary> trainerSummaries = trainerSummaryRepository.findByFirstNameAndLastName(firstName, lastName);
        if (trainerSummaries.isEmpty()) {
            log.info("No trainer summary found for first name: " + firstName + " and last name: " + lastName);
            throw new NotFoundException("No trainer summary found for first name: " + firstName + " and last name: " + lastName);
        }
        return trainerSummaries;
    }

    @Override
    public TrainerSummary findByUsername(String username) {
        if (username == null || username.isBlank())
            throw new NotFoundException("Username cannot be null");
        return trainerSummaryRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.info("No trainer summary found for username: " + username);
                    return new NotFoundException("No trainer summary found for username: " + username);
                });
    }

    @Override
    public TrainerSummary update(TrainerWorkloadRequestDTO trainerWorkload) {
        if (ActionType.DELETE.equals(trainerWorkload.getActionType()))
            trainerWorkload.setTrainingDuration(trainerWorkload.getTrainingDuration().negate());
        TrainerSummary trainerSummary = trainerSummaryRepository.findByUsername(trainerWorkload.getUsername())
                .orElseGet(() -> {
                    log.info("No trainer summary found for username: " + trainerWorkload.getUsername() + " creating new summary.");
                    return createTrainerSummary(trainerWorkload);
                });
        YearMonth yearMonth = YearMonth.from(trainerWorkload.getTrainingDate());
        Integer year = yearMonth.getYear();
        String month = yearMonth.getMonth().toString();
        BigDecimal duration = trainerWorkload.getTrainingDuration();
            trainerSummary.getMonthlySummary().computeIfAbsent(year, k -> new HashMap<>())
                    .merge(month, duration, BigDecimal::add);
        validateTrainingDuration(trainerSummary.getMonthlySummary());
        return trainerSummaryRepository.save(trainerSummary);
    }

    public TrainerSummary createTrainerSummary(TrainerWorkloadRequestDTO trainerWorkload) {
        TrainerSummary trainerSummary = new TrainerSummary();
        trainerSummary.setFirstName(trainerWorkload.getFirstName());
        trainerSummary.setLastName(trainerWorkload.getLastName());
        trainerSummary.setStatus(trainerWorkload.isActive());
        trainerSummary.setUsername(trainerWorkload.getUsername());
        trainerSummary.setMonthlySummary(new HashMap<>());
        return trainerSummary;
    }

    public void validateTrainingDuration(Map<Integer, Map<String, BigDecimal>> monthlySummary) {
        // Remove months with zero duration
        monthlySummary.values().forEach(monthMap ->
                monthMap.entrySet().removeIf(entry -> entry.getValue().compareTo(BigDecimal.ZERO) == 0)
        );

        // Remove years with zero duration
        monthlySummary.entrySet().removeIf(yearEntry -> {
            Map<String, BigDecimal> monthMap = yearEntry.getValue();
            return monthMap.isEmpty() || monthMap.values().stream().allMatch(duration -> duration.compareTo(BigDecimal.ZERO) == 0);
        });

        if (monthlySummary.values().stream()
                .flatMap(m -> m.values().stream())
                .anyMatch(duration -> duration.compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException("Training duration cannot be negative.");
        }
    }

}
