package com.epamlearning.reportmicroservice.services;

import com.epamlearning.reportmicroservice.entities.TrainerSummary;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;

public interface TrainerSummaryService {
  TrainerSummary calculate(String username);
}
