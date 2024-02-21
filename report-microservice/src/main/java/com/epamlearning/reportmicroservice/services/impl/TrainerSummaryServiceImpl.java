package com.epamlearning.reportmicroservice.services.impl;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import com.epamlearning.reportmicroservice.entities.TrainerWorkload;
import com.epamlearning.reportmicroservice.exceptions.NotFoundException;
import com.epamlearning.reportmicroservice.services.TrainerSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrainerSummaryServiceImpl implements TrainerSummaryService {

  private final TrainerWorkloadServiceImpl trainerWorkloadService;

  @Autowired
  public TrainerSummaryServiceImpl(TrainerWorkloadServiceImpl trainerWorkloadService) {
    this.trainerWorkloadService = trainerWorkloadService;
  }

  @Override
  public TrainerSummary calculate(String username) {
    List<TrainerWorkload> trainerWorkloads = trainerWorkloadService.getAllByUsername(username);
    if(trainerWorkloads.isEmpty()) {
      throw new NotFoundException("No trainer workload found for username to calculate summary: " + username);
    }
    Map<String, Map<String, BigDecimal>> monthlySummaryMap = new HashMap<>();
    HashSet<Integer> years = new HashSet<>();
    for(TrainerWorkload trainerWorkload : trainerWorkloads) {
      LocalDate trainingDate = trainerWorkload.getTrainingDate();
      YearMonth yearMonth = YearMonth.from(trainingDate);
      String month = yearMonth.getMonth().toString();
      String year = String.valueOf(yearMonth.getYear());
      years.add(yearMonth.getYear());

      BigDecimal duration = trainerWorkload.getTrainingDuration();

      monthlySummaryMap.computeIfAbsent(year, k -> new HashMap<>())
          .merge(month, duration, BigDecimal::add);
    }

    TrainerSummary trainerSummary = new TrainerSummary();
    trainerSummary.setFirstName(trainerWorkloads.getFirst().getFirstName());
    trainerSummary.setLastName(trainerWorkloads.getFirst().getLastName());
    trainerSummary.setStatus(trainerWorkloads.getFirst().isActive());
    trainerSummary.setUsername(username);
    trainerSummary.setYears(years.stream().sorted().toList());
    trainerSummary.setMonthlySummary(monthlySummaryMap);
    return trainerSummary;
  }

}
