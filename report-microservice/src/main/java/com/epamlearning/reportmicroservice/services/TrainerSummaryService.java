package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;

public interface TrainerSummaryService {
  TrainerSummary calculate(String username);
}
